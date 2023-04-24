package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String url);
}
