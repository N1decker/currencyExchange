package ru.nidecker.currencyExchange.exceptions;

public class CouldNotSaveEntity extends RuntimeException {
    public CouldNotSaveEntity(String message) {
        super(message);
    }
}
