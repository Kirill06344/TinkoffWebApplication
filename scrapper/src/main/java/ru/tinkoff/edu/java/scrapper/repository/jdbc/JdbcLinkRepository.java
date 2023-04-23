package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.DataChangeState;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkRepository implements LinkRepository {

    private static final String SQL_ADD_LINK =
            "insert into link(url, checked_at, updated_at, intr_count) values(?, ?, ?, ?) " +
                    "on conflict do nothing returning id, url, checked_at, updated_at, intr_count";

    private static final String SQL_UPDATE_CHECKED_AT_TIME = "update link set checked_at = ? where id = ?";

    private static final String SQL_UPDATE_DATA = "update link set updated_at = ?, intr_count = ? " +
            "where id = ? returning id, url, checked_at, updated_at, intr_count";

    private static final String SQL_FIND_ALL_OLD_LINKS = "select * from link where " +
            "extract(epoch from ? - link.checked_at) / 60 > 1";

    private static final String SQL_FIND_LINK_BY_URL = "select * from link where url = ?";

    private static final String SQL_FIND_LINK_BY_ID = "select * from link where id = ?";

    private static final String SQL_FIND_ALL_LINKS = "select * from link";

    private static final String SQL_DELETE_LINK_BY_ID = "delete from link where id = ?";

    private static final String SQL_DELETE_LINK_BY_URL = "delete from link where url = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkMapper = (rs, rowNum) -> new Link()
            .setId(rs.getLong("id"))
            .setUrl(rs.getString("url"))
            .setCheckedAt(rs.getObject("checked_at", LocalDateTime.class))
            .setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class))
            .setIntersectingCountField(rs.getLong("intr_count"));

    @Override
    public Optional<Link> add(Link entity) {
        if (entity == null) {
            return Optional.empty();
        }

        try {
            var res = jdbcTemplate.queryForObject(SQL_ADD_LINK,
                    linkMapper,
                    entity.getUrl(),
                    LocalDateTime.now(),
                    entity.getUpdatedAt(),
                    entity.getIntersectingCountField()
            );

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
    public int deleteById(Long id) {
        return jdbcTemplate.update(SQL_DELETE_LINK_BY_ID, id);
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
    public Optional<Link> findLinkById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_LINK_BY_ID, linkMapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteLinkByUrl(String url) {
        jdbcTemplate.update(SQL_DELETE_LINK_BY_URL, url);
    }

    @Override
    public List<Link> findAllOldLinks() {
        return jdbcTemplate.query(SQL_FIND_ALL_OLD_LINKS, linkMapper, LocalDateTime.now());
    }

    @Override
    public void updateCheckedAtTime(long id) {
        jdbcTemplate.update(SQL_UPDATE_CHECKED_AT_TIME, LocalDateTime.now(), id);
    }

    @Override
    public DataChangeState updateOtherData(long id, LocalDateTime updatedAt, long count) {
        var link = findLinkById(id);
        if (link.isEmpty()){
            return DataChangeState.NOTHING;
        }
        Link newData = jdbcTemplate.queryForObject(SQL_UPDATE_DATA, linkMapper, updatedAt, count, id);

        LocalDateTime prevDate = link.get().getUpdatedAt();
        LocalDateTime nextDate = newData.getUpdatedAt();

        long prevCount = link.get().getIntersectingCountField();
        long nextCount = newData.getIntersectingCountField();

        if (prevCount < nextCount) {
            return DataChangeState.COUNT;
        } else if (!prevDate.equals(nextDate)) {
            return DataChangeState.OTHER;
        } else {
            return DataChangeState.NOTHING;
        }
    }
}
