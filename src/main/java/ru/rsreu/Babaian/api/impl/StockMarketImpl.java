package ru.rsreu.Babaian.api.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rsreu.Babaian.OrderQueueHolder;
import ru.rsreu.Babaian.api.IStockMarket;
import ru.rsreu.Babaian.model.CurrencyPair;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class StockMarketImpl implements IStockMarket {

    @Getter
    private final OrderQueueHolder orderQueueHolder = new OrderQueueHolder();
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
        try {
            if (isBuy) {
                this.orderQueueHolder.buyOrders.put(order);
            } else this.orderQueueHolder.saleOrders.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
    public BlockingQueue<Order> getBuyQueue() {
        return this.orderQueueHolder.buyOrders;
    }

    @Override
    public BlockingQueue<Order> getSellQueue() {
        return this.orderQueueHolder.saleOrders;
    }

}
