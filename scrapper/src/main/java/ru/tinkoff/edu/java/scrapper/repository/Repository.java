package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.List;
import java.util.Optional;

public interface Repository<T, U> {

    Optional<Link> add(T entity);

    List<T> findAll();
    void deleteById(U id);
}
