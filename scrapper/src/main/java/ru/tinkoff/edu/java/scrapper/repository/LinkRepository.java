package ru.tinkoff.edu.java.scrapper.repository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends Repository<Link, Long> {
    Optional<Link> findLinkByUrl(String url);

    Optional<Link> findLinkById(long id);
    void deleteLinkByUrl(String url);

    List<Link> findAllOldLinks();

    void updateCheckedAtTime(long id);
}
