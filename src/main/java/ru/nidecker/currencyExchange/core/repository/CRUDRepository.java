package ru.nidecker.currencyExchange.core.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<E> {
    List<E> findAll();
    Optional<E> findById(Integer id);
    Optional<E> create(E e);
    Optional<E> update(E e);
    void delete(Integer id);
}
