package ru.rsreu.Babaian.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.rsreu.Babaian.model.enums.Currency;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class User {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock  = readWriteLock.readLock();
    private final Lock writeLock  = readWriteLock.writeLock();

    @Getter
    private final Long numId;
    private final Map<Currency, Double> balance = new HashMap<>();
    private final ArrayList<TradeResult> trades = new ArrayList<>();
    private final ArrayList<Order> userOrders = new ArrayList<>();

    public Double getBalanceCurrency(Currency currency){
        readLock.lock();
        double val = 0;
        if(balance.containsKey(currency))
            val = balance.get(currency);
        readLock.unlock();
        return val;
    }

    public boolean hasSufficientCurrency(Currency currency, double amount){
        boolean flag = false;
        readLock.lock();
        if(this.balance.containsKey(currency) && this.balance.get(currency) >= amount){
            flag = true;
        }
        readLock.unlock();
        return flag;
    }

    public boolean decreaseBalance(Currency currency, double amount){
        boolean flag = false;
        writeLock.lock();
        if(this.balance.containsKey(currency) && hasSufficientCurrency(currency, amount)) {
            this.balance.put(currency, this.balance.get(currency) - amount);
            flag = true;
        }
        writeLock.unlock();
        return flag;
    }

    public void increaseBalance(Currency currency, double amount){
        writeLock.lock();
        if(this.balance.containsKey(currency))
            this.balance.put(currency, this.balance.get(currency) + amount);
        else this.balance.put(currency, amount);
        writeLock.unlock();
    }

    public String getStatus(){
        readLock.lock();
        StringBuilder stringBuilder = new StringBuilder();
        for (Currency currency: balance.keySet()){
            stringBuilder.append(currency.toString() + ": " + balance.get(currency) + "\n");
        }
        readLock.unlock();
        return stringBuilder.toString();
    }

    public Map<Currency, Double> getBalance() {
        readLock.lock();
        var res = balance;
        readLock.unlock();
        return res;
    }

    public void addOrder(Order order){
        writeLock.lock();
        userOrders.add(order);
        writeLock.unlock();
    }

    public ArrayList<Order> getOpenOrders(){
        readLock.lock();
        var ls = userOrders;
        readLock.unlock();
        return ls;
    }

    public void addTradeRes(TradeResult tr){
        writeLock.lock();
        trades.add(tr);
        writeLock.unlock();
    }

    public ArrayList<TradeResult> getTradesResults(){
        readLock.lock();
        var ls = trades;
        readLock.unlock();
        return ls;
    }

    public void removeOrder(Order order){
        writeLock.lock();
        userOrders.remove(order);
        writeLock.unlock();
    }



}
