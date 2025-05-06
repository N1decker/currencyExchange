package ru.nidecker.currencyExchange.exceptions;

public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException(String message) {
        super(message);
    }
}
