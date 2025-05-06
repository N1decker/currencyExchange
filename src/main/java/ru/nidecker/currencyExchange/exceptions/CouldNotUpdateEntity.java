package ru.nidecker.currencyExchange.exceptions;

public class CouldNotUpdateEntity extends RuntimeException {
    public CouldNotUpdateEntity(String message) {
        super(message);
    }
}
