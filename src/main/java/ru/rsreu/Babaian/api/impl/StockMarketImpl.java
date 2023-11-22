package ru.rsreu.Babaian.api.impl;

import lombok.Getter;
import ru.rsreu.Babaian.OrdersHolder;
import ru.rsreu.Babaian.api.IStockMarket;
import ru.rsreu.Babaian.model.CurrencyPair;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.OrderAvailable;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.util.Map;

public class StockMarketImpl implements IStockMarket {

    private final OrdersHolder ordersHolder = new OrdersHolder();

    @Override
    public User createUser(Long id) {
        return new User(id);
    }

    @Override
    public void increaseBalance(User user, Currency currency, double amount) {
        user.increaseBalance(currency, amount);

    }

    @Override
    public void decreaseBalance(User user, Currency currency, double amount) {
        user.decreaseBalance(currency, amount);
    }

    @Override
    public Order createOrder(Long id, User user, Currency currencyBase, Currency currencyQuote, Double quantity, Double price, boolean isBuy) {
        CurrencyPair currencyPair = new CurrencyPair(currencyBase, currencyQuote);
        Order order = new Order(id, user, currencyPair, quantity, price, isBuy);
        user.addOrder(order);
        long seq = 0;
        OrderAvailable or;
        //while (true) {
            seq = ordersHolder.getBuyOrders().getRingBuffer().next();
            or = ordersHolder.getBuyOrders().getRingBuffer().get(seq);
            if (or.getOrder().getStatus() == OrderStatus.FULFILLED) {
                or.setOrder(order);
                //break;
            }
        //}
        this.ordersHolder.getBuyOrders().getRingBuffer().publish(seq);

//            long orid = ordersHolder.getBuyOrders().getRingBuffer().getCursor();
//            or = ordersHolder.getBuyOrders().getRingBuffer().get(orid);
//            this.ordersHolder.getBuyOrders().getRingBuffer().publish(orid);
//            this.ordersHolder.getBuyOrders().getRingBuffer().publish();

        return order;

    }

    @Override
    public String getStatus(User user) {
        return user.getStatus();
    }

    @Override
    public Map<Currency, Double> getBalance(User user) {
        return user.getBalance();
    }

    @Override
    public OrdersHolder getOrderHolder() {
        return ordersHolder;
    }

//    @Override
//    public BlockingQueue<Order> getBuyQueue() {
//        return this.ordersHolder.buyOrders;
//    }
//
//    @Override
//    public BlockingQueue<Order> getSellQueue() {
//        return this.ordersHolder.saleOrders;
//    }

}
