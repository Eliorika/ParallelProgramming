package ru.rsreu.Babaian.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rsreu.Babaian.model.enums.Currency;

@Getter
@AllArgsConstructor
public class CurrencyPair {
    private Currency baseCurrency;
    private Currency quoteCurrency;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyPair currencyPair = (CurrencyPair) o;
        if(this.baseCurrency.equals(currencyPair.baseCurrency) && this.quoteCurrency.equals(currencyPair.quoteCurrency))
            return true;
        return false;
    }

}
