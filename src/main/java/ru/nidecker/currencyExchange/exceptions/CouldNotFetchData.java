package ru.nidecker.currencyExchange.exceptions;

public class CouldNotFetchData extends RuntimeException {
    public CouldNotFetchData(String message) {
        super(message);
    }
}
