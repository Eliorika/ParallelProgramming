package ru.rsreu.Babaian.model;

import lombok.Getter;
import lombok.Setter;

public class OrderAvailable {

    @Getter
    @Setter
    private Order order = new Order();

    public static OrderAvailable buyOrder(){
        return new OrderAvailable();
    }

    public static OrderAvailable sellOrder(){
        return new OrderAvailable();
    }
}
