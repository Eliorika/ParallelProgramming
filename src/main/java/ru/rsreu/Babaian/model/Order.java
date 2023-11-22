package ru.rsreu.Babaian.model;

import lombok.Data;
import ru.rsreu.Babaian.model.enums.Currency;
import ru.rsreu.Babaian.model.enums.OrderStatus;

@Data
public class Order {
    private Long id;
    private User user;
    private CurrencyPair currencyPair = new CurrencyPair(Currency.DOLLAR, Currency.DOLLAR);
    private Double quantity;
    private Double price;
    private boolean isBuy;
    private OrderStatus status = OrderStatus.FULFILLED;

    public Order(Long id, User user, CurrencyPair currencyPair, Double quantity, Double price, boolean isBuy){
        this.id = id;
        this.user = user;
        this.currencyPair = currencyPair;
        this.quantity = quantity;
        this.price = price;
        this.isBuy = isBuy;
        this.status = OrderStatus.PENDING;
    }

    public Order() {

    }

    @Override
    public String toString(){
        return "id = " + id
                + "\nuser: " + user.getNumId()
                + "\ncurrency: " + currencyPair.toString()
                + "\nquantity: " + quantity
                + "\nisBuy: " + isBuy
                + "\nstatus: " + status;
    }

    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        var inOr = (Order) obj;

        if ((inOr.isBuy != this.isBuy) || inOr.status != this.status || !inOr.quantity.equals(this.quantity)
        || !inOr.currencyPair.equals(this.currencyPair))
            return false;
        return true;

    }

}
