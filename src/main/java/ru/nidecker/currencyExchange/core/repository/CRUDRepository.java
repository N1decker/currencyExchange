package ru.nidecker.currencyExchange.core.repository;

import java.util.List;

public interface CRUDRepository<E> {
    List<E> findAll();
    E findById(Integer id);
    E create(E e);
    E update(E e);
    void delete(Integer id);
}
