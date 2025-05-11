package ru.nidecker.currencyExchange.core.service;

import java.util.List;

public interface Service<E, D> {
    List<D> findAll();
    D findById(Integer id);
    D create(E e);
    D update(E e);
    void delete(Integer id);
}
