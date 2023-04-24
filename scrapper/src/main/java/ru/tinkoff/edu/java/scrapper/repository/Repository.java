package ru.tinkoff.edu.java.scrapper.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, U> {

    Optional<T> add(T entity);

    List<T> findAll();
    int deleteById(U id);
}
