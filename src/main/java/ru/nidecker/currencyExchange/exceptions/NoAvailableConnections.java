package ru.nidecker.currencyExchange.exceptions;

public class NoAvailableConnections extends RuntimeException {
    public NoAvailableConnections(String message) {
        super(message);
    }
}
