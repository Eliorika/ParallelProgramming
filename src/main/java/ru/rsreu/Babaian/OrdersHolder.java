package ru.rsreu.Babaian;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.Getter;
import ru.rsreu.Babaian.model.Order;
import ru.rsreu.Babaian.model.OrderAvailable;
import ru.rsreu.Babaian.model.TradeResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrdersHolder {
//    public final BlockingQueue<Order> buyOrders = new LinkedBlockingQueue<>();
//    public final BlockingQueue<Order> saleOrders = new LinkedBlockingQueue<>();
//
    public final BlockingQueue<TradeResult> results = new LinkedBlockingQueue<>();

    private int bufferSize = 1024;

    @Getter
    private final Disruptor<OrderAvailable> buyOrders = new Disruptor<>(
            OrderAvailable::buyOrder,
            bufferSize,
            DaemonThreadFactory.INSTANCE,
            ProducerType.MULTI,
            new BusySpinWaitStrategy()
    );
}
