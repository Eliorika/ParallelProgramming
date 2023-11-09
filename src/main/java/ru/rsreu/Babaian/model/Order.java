package ru.rsreu.Babaian.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rsreu.Babaian.OrderQueueHolder;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Data
public class Order {
    private final Long id;
    private final User user;
    private final CurrencyPair currencyPair;
    private Double quantity;
    private Double price;
    private final boolean isBuy;
    private OrderStatus status;

    public Order(Long id, User user, CurrencyPair currencyPair, Double quantity, Double price, boolean isBuy){
        this.id = id;
        this.user = user;
        this.currencyPair = currencyPair;
        this.quantity = quantity;
        this.price = price;
        this.isBuy = isBuy;
        this.status = OrderStatus.PENDING;
    }

}
