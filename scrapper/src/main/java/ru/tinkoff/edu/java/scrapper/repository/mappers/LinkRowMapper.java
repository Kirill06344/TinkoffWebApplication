package ru.tinkoff.edu.java.scrapper.repository.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link()
                .setId(rs.getLong("id"))
                .setUrl(rs.getString("url"))
                .setCheckedAt(rs.getObject("checked_at", LocalDateTime.class))
                .setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .setIntersectingCountField(rs.getLong("intr_count"));
    }
}
