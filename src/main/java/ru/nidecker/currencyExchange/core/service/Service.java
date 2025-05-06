package ru.nidecker.currencyExchange.core.service;

import java.util.List;

public interface Service<E> {
    List<E> findAll();
    E findById(Integer id);
    E create(E e);
    E update(E e);
    void delete(Integer id);
}
