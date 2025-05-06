package ru.nidecker.currencyExchange.exceptions;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message);
    }
}
