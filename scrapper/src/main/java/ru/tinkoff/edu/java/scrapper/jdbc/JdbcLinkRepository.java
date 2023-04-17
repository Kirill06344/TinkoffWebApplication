package ru.tinkoff.edu.java.scrapper.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.mappers.LinkRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkRepository implements LinkRepository {

    private static final String SQL_ADD_LINK = "insert into link(url) values(?) on conflict do nothing returning id,url";

    private static final String SQL_FIND_LINK_BY_URL = "select * from link where url = ?";

    private static final String SQL_FIND_ALL_LINKS = "select * from link";

    private static final String SQL_DELETE_LINK_BY_ID = "delete from link where id = ?";

    private static final String SQL_DELETE_LINK_BY_URL = "delete from link where url = ?";

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkMapper;

    @Override
    public Optional<Link> add(Link entity) {
        if (entity == null) {
            return Optional.empty();
        }

        try {
            var res = jdbcTemplate.queryForObject(SQL_ADD_LINK, linkMapper, entity.getUrl());
            return Optional.ofNullable(res);
        } catch (DataAccessException ex) {
            log.error(ex.getMessage());
            return findLinkByUrl(entity.getUrl());
        }
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL_LINKS, linkMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_LINK_BY_ID, id);
    }

    @Override
    public Optional<Link> findLinkByUrl(String url) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_LINK_BY_URL, linkMapper, url));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }

    }

    @Override
    public void deleteLinkByUrl(String url) {
        jdbcTemplate.update(SQL_DELETE_LINK_BY_URL, url);
    }
}
