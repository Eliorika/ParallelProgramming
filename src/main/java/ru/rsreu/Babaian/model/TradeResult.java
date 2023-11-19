package ru.rsreu.Babaian.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TradeResult {
    private Order orderBuyer;
    private Order orderSeller;
    private double executedQuantity;
    private String message;
    private Date date;

}
