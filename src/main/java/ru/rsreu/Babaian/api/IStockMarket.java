package ru.rsreu.Babaian.api;

import ru.rsreu.Babaian.OrderQueueHolder;
import ru.rsreu.Babaian.model.CurrencyPair;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.Currency;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public interface IStockMarket {

    User createUser(Long id);
    void increaseBalance(User user, Currency currency, double amount);

    void decreaseBalance(User user, Currency currency, double amount);

    Order createOrder(Long id, User user, Currency currencyBase, Currency currencyQuote, Double quantity, Double price, boolean isBuy);

    String getStatus(User user);

    Map<Currency, Double> getBalance(User user);

    BlockingQueue<Order> getBuyQueue();

    BlockingQueue<Order> getSellQueue();

    OrderQueueHolder getOrderQueueHolder();

}
