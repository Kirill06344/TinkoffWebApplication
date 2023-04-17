package ru.tinkoff.edu.java.scrapper.repository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.Optional;

public interface LinkRepository extends Repository<Link, Long> {
    Optional<Link> findLinkByUrl(String url);
    void deleteLinkByUrl(String url);
}
