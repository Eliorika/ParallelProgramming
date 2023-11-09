package ru.rsreu.Babaian.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rsreu.Babaian.OrderQueueHolder;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
public class ProceedTrade implements Runnable{
    public final ReentrantLock lock = new ReentrantLock();
    private final OrderQueueHolder orderQueueHolder;

    private  List<Order> findMatchingOrders(Order order, BlockingQueue<Order> orderQueue){
        List<Order> matchingOrders = new ArrayList<>();

        for(Order existingOrder : orderQueue){
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

    public  OrderStatus processBuyerOrder(Order order, BlockingQueue<Order> sellOrderQueue) throws InterruptedException {
        List<Order> matchingOrders = findMatchingOrders(order, sellOrderQueue);
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
                    Math.min(availableAmount, totalAmount): Math.max(availableAmount, totalAmount);

            // Выполняем сделку и обновляем балансы клиентов
            if(executeTrade(order, matchingOrder, tradeQuantity, tradeAmount)){
                if(matchingOrder.getQuantity() == tradeQuantity){
                    sellOrderQueue.remove(matchingOrder);
                    matchingOrder.setStatus(OrderStatus.FULFILLED);
                } else {
                    matchingOrder.setQuantity(matchingOrder.getQuantity() - tradeQuantity);
                    matchingOrder.setStatus(OrderStatus.PARTIAL);
                }
                orderQueueHolder.results.add(new TradeResult(order, tradeQuantity, "Proceed", new Date()));
            } else {
                //orderQueueHolder.results.add(new TradeResult(order, tradeQuantity, "Error", new Date()));
            }

            // Обновляем статус ордеров
//            updateOrderStatus(order, tradeQuantity, tradeAmount);
//            updateOrderStatus(matchingOrder, tradeQuantity, tradeAmount);

            totalQuantity -= tradeQuantity;


            if (totalQuantity == 0) {
                order.setStatus(OrderStatus.FULFILLED);
                orderQueueHolder.buyOrders.remove(order);
                break;
            }
        }

        if (totalQuantity > 0) {
            order.setStatus(OrderStatus.PARTIAL);
            order.setQuantity(totalQuantity);
        }

        return order.getStatus();
    }


    private  boolean executeTrade(Order order, Order matchingOrder, double quantity, Double tradePrice) {
        User buyer = order.getUser();
        User seller = matchingOrder.getUser();
        boolean flag = false;
        lock.lock();
        // Проверка, что у покупателя и продавца достаточно средств и валюты для сделки
        if (buyer.hasSufficientCurrency(order.getCurrencyPair().getQuoteCurrency(), tradePrice*quantity)
                && seller.hasSufficientCurrency(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity)) {

            // Учитываем обмен валюты и изменение балансов покупателя и продавца
            buyer.decreaseBalance(matchingOrder.getCurrencyPair().getQuoteCurrency(), tradePrice*quantity);
            seller.increaseBalance(matchingOrder.getCurrencyPair().getQuoteCurrency(), tradePrice*quantity);

            buyer.increaseBalance(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity);
            seller.decreaseBalance(matchingOrder.getCurrencyPair().getBaseCurrency(), quantity);

            // Записываем информацию о сделке, если необходимо
            System.out.println("buyer: " + buyer.getNumId()
                                +"\nseller: " + seller.getNumId()
                                +"\nbase currency: " + order.getCurrencyPair().getBaseCurrency()
                                +"\nquote currency: " + order.getCurrencyPair().getQuoteCurrency()
                                +"\nquantity: " + quantity
                                +"\nprice: " + tradePrice +"\n\n");
            flag = true;


        }

        lock.unlock();
        return flag;
    }


    @Override
    public void run() {
        try {
            while (true) {
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                processBuyerOrder(orderQueueHolder.buyOrders.take(), orderQueueHolder.saleOrders);
            }
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
    }
}
