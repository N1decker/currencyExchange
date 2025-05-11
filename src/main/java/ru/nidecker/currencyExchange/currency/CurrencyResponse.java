package ru.nidecker.currencyExchange.currency;

public class CurrencyResponse {
    private final int id;
    private final String name;
    private final String code;
    private final String sign;

    public CurrencyResponse(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }
}
