package ru.nidecker.currencyExchange.exchangeRate;

import java.math.BigDecimal;

public class ExchangeRateRequest {
    private final String baseCurrency;
    private final String targetCurrency;
    private final BigDecimal rate;

    public ExchangeRateRequest(String baseCurrency, String targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
