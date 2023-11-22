package ru.rsreu.Babaian.trade;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;

import ru.rsreu.Babaian.OrdersHolder;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.OrderAvailable;
import ru.rsreu.Babaian.model.TradeResult;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
public class ProceedTrade implements Runnable, EventHandler<OrderAvailable> {
    public final ReentrantLock lock = new ReentrantLock();
    private final OrdersHolder ordersHolder;

    private List<Order> findMatchingOrders(Order order) {
        List<Order> matchingOrders = new ArrayList<>();
        long sequence = ordersHolder.getBuyOrders().getRingBuffer().getCursor();
        for (long i = 0; i <= sequence; i++) {
            var existingOrder = ordersHolder.getBuyOrders().getRingBuffer().get(i).getOrder();
            if (existingOrder != order &&
                    existingOrder.getCurrencyPair().equals(order.getCurrencyPair()) &&
                    existingOrder.isBuy() != order.isBuy() &&
                    existingOrder.getStatus() != OrderStatus.FULFILLED &&
                    order.getUser().getNumId() != existingOrder.getUser().getNumId()) {

                if (order.isBuy()) {
                    if (existingOrder.getPrice().compareTo(order.getPrice()) <= 0) {
                        matchingOrders.add(existingOrder);
                    }
                } else {
                    if (existingOrder.getPrice().compareTo(order.getPrice()) >= 0) {
                        matchingOrders.add(existingOrder);
                    }
                }
            }
        }

        return matchingOrders;
    }

    public OrderStatus processBuyerOrder(Order order) throws InterruptedException {
        List<Order> matchingOrders = findMatchingOrders(order);
        if (matchingOrders.isEmpty()) {
            return null;
        }


        double totalQuantity = order.getQuantity();
        Double totalAmount = order.getPrice();

        for (Order matchingOrder : matchingOrders) {
            double availableQuantity = matchingOrder.getQuantity();
            Double availableAmount = matchingOrder.getPrice();

            double tradeQuantity = Math.min(totalQuantity, availableQuantity);
            Double tradeAmount = order.isBuy() ?
                    Math.min(availableAmount, totalAmount) : Math.max(availableAmount, totalAmount);

            // Выполняем сделку и обновляем балансы клиентов
            if (executeTrade(order, matchingOrder, tradeQuantity, tradeAmount)) {
                if (matchingOrder.getQuantity() == tradeQuantity) {
                    //this.ordersHolder.saleOrders.remove(matchingOrder);
                    matchingOrder.getUser().removeOrder(matchingOrder);

                    matchingOrder.setStatus(OrderStatus.FULFILLED);
                } else {
                    matchingOrder.setQuantity(matchingOrder.getQuantity() - tradeQuantity);
                    matchingOrder.setStatus(OrderStatus.PARTIAL);
                }
                var tr = new TradeResult(order, matchingOrder, tradeQuantity, "Proceed", new Date());
                ordersHolder.results.add(tr);
                matchingOrder.getUser().addTradeRes(tr);
                order.getUser().addTradeRes(tr);
            } else {
                //ordersHolder.results.add(new TradeResult(order, tradeQuantity, "Error", new Date()));
            }

            // Обновляем статус ордеров
//            updateOrderStatus(order, tradeQuantity, tradeAmount);
//            updateOrderStatus(matchingOrder, tradeQuantity, tradeAmount);

            totalQuantity -= tradeQuantity;


            if (totalQuantity == 0) {

                order.setStatus(OrderStatus.FULFILLED);
                order.getUser().removeOrder(order);
                //ordersHolder.buyOrders.remove(order);
                break;
            }
        }

        if (totalQuantity > 0) {
            order.setStatus(OrderStatus.PARTIAL);
            order.setQuantity(totalQuantity);
        }

        return order.getStatus();
    }


    private boolean executeTrade(Order order, Order matchingOrder, double quantity, Double tradePrice) {
        User buyer;
        User seller;
        if (order.isBuy()) {
            buyer = order.getUser();
            seller = matchingOrder.getUser();
        } else {
            seller = order.getUser();
            buyer = matchingOrder.getUser();
        }

        boolean flag = false;
        lock.lock();
        // Проверка, что у покупателя и продавца достаточно средств и валюты для сделки
        if (buyer.hasSufficientCurrency(order.getCurrencyPair().getQuoteCurrency(), tradePrice * quantity)
                && seller.hasSufficientCurrency(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity)) {

            // Учитываем обмен валюты и изменение балансов покупателя и продавца
            buyer.decreaseBalance(matchingOrder.getCurrencyPair().getQuoteCurrency(), tradePrice * quantity);
            seller.increaseBalance(matchingOrder.getCurrencyPair().getQuoteCurrency(), tradePrice * quantity);

            buyer.increaseBalance(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity);
            seller.decreaseBalance(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity);

            // Записываем информацию о сделке, если необходимо
            System.out.println("buyer: " + buyer.getNumId()
                    + "\nseller: " + seller.getNumId()
                    + "\nbase currency: " + order.getCurrencyPair().getBaseCurrency()
                    + "\nquote currency: " + order.getCurrencyPair().getQuoteCurrency()
                    + "\nquantity: " + quantity
                    + "\nprice: " + tradePrice + "\n\n");
            flag = true;


        }

        lock.unlock();
        return flag;
    }


    @Override
    public void run() {
        this.ordersHolder.getBuyOrders().handleEventsWith(this);
        this.ordersHolder.getBuyOrders().start();
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                //this.ordersHolder.getBuyOrders().shutdown();
                break;
            }
            //processBuyerOrder();
        }
    }

    @Override
    public void onEvent(OrderAvailable orderAvailable, long l, boolean b) throws Exception {
        //if (orderAvailable.getOrder().isBuy())
        processBuyerOrder(orderAvailable.getOrder());
    }

}
