package ru.rsreu.Babaian;

import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.TradeResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderQueueHolder {
    public final BlockingQueue<Order> buyOrders = new LinkedBlockingQueue<>();
    public final BlockingQueue<Order> saleOrders = new LinkedBlockingQueue<>();

    public final BlockingQueue<TradeResult> results = new LinkedBlockingQueue<>();
}
