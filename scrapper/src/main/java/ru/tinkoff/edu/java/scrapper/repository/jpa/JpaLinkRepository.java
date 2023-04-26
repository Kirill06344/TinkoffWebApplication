package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String url);

    @Query(value = "select * from link l where extract(epoch from :now - l.checked_at) / 60 > 1 ", nativeQuery = true)
    List<Link> findAllOldLinks(@Param("now") LocalDateTime currentTime);

}
