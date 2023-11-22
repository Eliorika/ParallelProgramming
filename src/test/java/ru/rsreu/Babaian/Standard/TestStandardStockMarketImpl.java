package ru.rsreu.Babaian.Standard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.api.IStockMarket;
import ru.rsreu.Babaian.api.impl.StockMarketImpl;
import ru.rsreu.Babaian.model.CurrencyPair;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.trade.ProceedTrade;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

public class TestStandardStockMarketImpl {
    @Test
    public void testCreateUser(){
        IStockMarket stockMarket = new StockMarketImpl();
        Long id = 1L;

        Assertions.assertEquals(id, stockMarket.createUser(id).getNumId());
    }

    @Test
    public void testIncreaseDecreaseBalance(){
        IStockMarket stockMarket = new StockMarketImpl();
        User user = stockMarket.createUser(1L);
        stockMarket.increaseBalance(user, Currency.DOLLAR, 5);
        Assertions.assertEquals(5, user.getBalance().get(Currency.DOLLAR));

        stockMarket.increaseBalance(user, Currency.DOLLAR, 1);
        Assertions.assertEquals(6, user.getBalance().get(Currency.DOLLAR));

        stockMarket.decreaseBalance(user, Currency.DOLLAR, 4);
        Assertions.assertEquals(2, user.getBalance().get(Currency.DOLLAR));
    }

    @Test
    public void createOrder(){
        IStockMarket stockMarket = new StockMarketImpl();
        User user = stockMarket.createUser(1L);
        Order order = new Order(1l, user, new CurrencyPair(Currency.DOLLAR, Currency.EURO), 5d, 1d, true);
        CurrencyPair currencyPair = new CurrencyPair(Currency.DOLLAR, Currency.EURO);
        Order newOrder = new Order(1l, user, currencyPair, 5d, 1d, true);
        Assertions.assertEquals(newOrder, order);
    }

    @Test
    public void testGetStatus(){
        IStockMarket stockMarket = new StockMarketImpl();
        User user = stockMarket.createUser(1L);
        stockMarket.increaseBalance(user, Currency.DOLLAR, 10);
        stockMarket.increaseBalance(user, Currency.YUAN, 8);
        Assertions.assertEquals(10, stockMarket.getBalance(user).get(Currency.DOLLAR));
        Assertions.assertEquals(8, stockMarket.getBalance(user).get(Currency.YUAN));
    }

    @Test
    public void testGetOrders(){
        IStockMarket stockMarket = new StockMarketImpl();
        User user = stockMarket.createUser(1L);
        Order order1 = stockMarket.createOrder(1l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, true);
        Order order2 = stockMarket.createOrder(2l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, true);


        Assertions.assertEquals(order1, user.getOpenOrders().get(0));
        Assertions.assertEquals(order2, user.getOpenOrders().get(1));

        Order order3 = stockMarket.createOrder(3l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, false);
        Order order4 = stockMarket.createOrder(4l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, false);

        Assertions.assertEquals(order3, user.getOpenOrders().get(2));
        Assertions.assertEquals(order4, user.getOpenOrders().get(3));

    }


    @Test
    public void testTrade(){
        IStockMarket stockMarket = new StockMarketImpl();
        ProceedTrade proceedTrade = new ProceedTrade(stockMarket.getOrderHolder());
        User user1 = stockMarket.createUser(1L);
        Order order1 = stockMarket.createOrder(1l, user1, Currency.DOLLAR, Currency.EURO, 6d, 2d, true);
        stockMarket.increaseBalance(user1, Currency.EURO, 60);

        User user2 = stockMarket.createUser(2l);
        stockMarket.increaseBalance(user2, Currency.DOLLAR, 40);
        Order order2 = stockMarket.createOrder(2l, user2, Currency.DOLLAR, Currency.EURO, 5d, 2d, false);
        Order order3 = stockMarket.createOrder(3l, user2, Currency.DOLLAR, Currency.EURO, 3d, 1d, false);
        Order order4 = stockMarket.createOrder(4l, user2, Currency.DOLLAR, Currency.YUAN, 3d, 1d, false);

        try {
            proceedTrade.processBuyerOrder(order1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Assertions.assertEquals(OrderStatus.FULFILLED, order1.getStatus());
        Assertions.assertEquals(OrderStatus.FULFILLED, order2.getStatus());
        Assertions.assertEquals(OrderStatus.PARTIAL, order3.getStatus());
        Assertions.assertEquals(OrderStatus.PENDING, order4.getStatus());
        Assertions.assertEquals(60,user1.getBalance().get(Currency.EURO) + user2.getBalance().get(Currency.EURO));
        Assertions.assertEquals(40,user1.getBalance().get(Currency.DOLLAR) + user2.getBalance().get(Currency.DOLLAR));
    }






}
