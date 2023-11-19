package ru.rsreu.Babaian.Threads;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.OrderQueueHolder;
import ru.rsreu.Babaian.api.IStockMarket;
import ru.rsreu.Babaian.api.impl.StockMarketImpl;
import ru.rsreu.Babaian.model.CurrencyPair;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.ProceedTrade;
import ru.rsreu.Babaian.model.User;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestThreadsStockMarketImpl {
    @Test
    public void testIncreaseDecreaseBalance() {
        IStockMarket stockMarket = new StockMarketImpl();
        User user = stockMarket.createUser(1L);

        Thread th1 = new Thread(() -> stockMarket.increaseBalance(user, Currency.DOLLAR, 5)
        );

        Thread th2 = new Thread(() -> stockMarket.increaseBalance(user, Currency.DOLLAR, 1)
        );

        Thread th3 = new Thread(() -> {
            try {
                th1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stockMarket.decreaseBalance(user, Currency.DOLLAR, 4);}
        );

        th1.start();
        th2.start();
        th3.start();

        try {
            th1.join();
            th2.join();
            th3.join();
            Assertions.assertEquals(2, stockMarket.getBalance(user).get(Currency.DOLLAR));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testGetOrders() {
        IStockMarket stockMarket = new StockMarketImpl();
        try {
            User user = stockMarket.createUser(1L);
            Order order1 = new Order(1l, user, new CurrencyPair(Currency.DOLLAR, Currency.EURO), 5d, 1d, true);
            Thread th1 = new Thread(() -> stockMarket.createOrder(1l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, true));

            Order order2 = new Order(2l, user, new CurrencyPair( Currency.DOLLAR, Currency.EURO), 5d, 1d, true);
            Thread th2 = new Thread(() -> stockMarket.createOrder(2l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, true));

            th1.start();
            th2.start();

            th1.join();
            th2.join();

            Assertions.assertEquals(order1, user.getOpenOrders().get(0));
            Assertions.assertEquals(order2, user.getOpenOrders().get(1));


            Order order3 = new Order(3l, user,new CurrencyPair(Currency.DOLLAR, Currency.EURO), 5d, 1d, false);
            Thread th3 = new Thread(() -> stockMarket.createOrder(3l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, false));

            Order order4 = new Order(4l, user, new CurrencyPair(Currency.DOLLAR, Currency.EURO), 5d, 1d, false);
            Thread th4 = new Thread(() -> stockMarket.createOrder(4l, user, Currency.DOLLAR, Currency.EURO, 5d, 1d, false));

            th3.start();
            th4.start();

            th3.join();
            th4.join();

            Assertions.assertEquals(order3, user.getOpenOrders().get(2));
            Assertions.assertEquals(order4, user.getOpenOrders().get(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testTrade() {
        IStockMarket stockMarket = new StockMarketImpl();
        List<Thread> allThIncr = new ArrayList<>();
        List<Thread> allThOrder = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();

        int n = 100;
        double dollarBal = 100;
        double euroBal = 90;
        double yuanBal = 30;
        double jenaBal = 30;
        double rubbleBal = 80;

        for(int i = 1; i <= n; i++){
            User user = stockMarket.createUser((long) i);
            allUsers.add(user);
            allThIncr.add(new Thread(() -> {
                stockMarket.increaseBalance(user, Currency.EURO, euroBal);
                stockMarket.increaseBalance(user, Currency.DOLLAR, dollarBal);
                stockMarket.increaseBalance(user, Currency.JENA, jenaBal);
                stockMarket.increaseBalance(user, Currency.YUAN, yuanBal);
                stockMarket.increaseBalance(user, Currency.RUBLE, rubbleBal);
            }));

            allThOrder.add(new Thread(() -> {
                Random random = new Random();
                stockMarket.createOrder(1l, user, Currency.DOLLAR, Currency.EURO, random.nextDouble(50), random.nextDouble(10), true);
                stockMarket.createOrder(2l, user, Currency.EURO, Currency.RUBLE, random.nextDouble(50), random.nextDouble(10), true);
                stockMarket.createOrder(3l, user, Currency.DOLLAR, Currency.EURO, random.nextDouble(50), random.nextDouble(10), false);
                stockMarket.createOrder(4l, user, Currency.EURO, Currency.RUBLE, random.nextDouble(50), random.nextDouble(10), false);
            }));
        }


        try {
            Thread thr = new Thread(new ProceedTrade(stockMarket.getOrderQueueHolder()));
            thr.start();

            allThIncr.stream().forEach(Thread::start);
            allThOrder.stream().forEach(Thread::start);

            for (Thread th: allThIncr) {
                th.join();
            }

            for (Thread th: allThOrder) {
                th.join();
            }

            var order = stockMarket.createOrder(1l, allUsers.get(0), Currency.DOLLAR, Currency.EURO, 1d, 1d, true);
            stockMarket.createOrder(3l, allUsers.get(1), Currency.DOLLAR, Currency.EURO, 1d, 1d, false);
            long startTime = System.currentTimeMillis();


            //Thread.sleep(10000);
            try {
                while(stockMarket.getOrderQueueHolder().buyOrders.contains(order)) {}

                thr.interrupt();
                thr.join();
            } catch (InterruptedException e){

            }

            long endTime = System.currentTimeMillis();

            double dollarBalAct = 0;
            double euroBalAct = 0;
            double yuanBalAct = 0;
            double jenaBalAct = 0;
            double rubbleBalAct = 0;

            for (User user: allUsers) {
                dollarBalAct+=user.getBalanceCurrency(Currency.DOLLAR);
                euroBalAct+=user.getBalanceCurrency(Currency.EURO);
                yuanBalAct+=user.getBalanceCurrency(Currency.YUAN);
                jenaBalAct+=user.getBalanceCurrency(Currency.JENA);
                rubbleBalAct+=user.getBalanceCurrency(Currency.RUBLE);
            }

            Assertions.assertEquals(dollarBal*n, dollarBalAct, 1e-5);
            Assertions.assertEquals(euroBal*n, euroBalAct, 1e-5);
            Assertions.assertEquals(yuanBal*n, yuanBalAct, 1e-5);
            Assertions.assertEquals(jenaBal*n, jenaBalAct, 1e-5);
            Assertions.assertEquals(rubbleBal*n, rubbleBalAct, 1e-5);

            long executionTime = (endTime - startTime) / 1000;

            int s = stockMarket.getOrderQueueHolder().results.size();
            if(executionTime!=0)
                System.out.println("Average orders/sec: " + s/executionTime);
            else System.out.println("Average orders/sec: " + s);
        } catch (InterruptedException e) {
            System.out.println("Thread is interrupted");
        }
    }


}
