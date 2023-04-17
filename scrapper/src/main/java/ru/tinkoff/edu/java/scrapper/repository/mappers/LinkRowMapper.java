package ru.tinkoff.edu.java.scrapper.repository.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link()
                .setId(rs.getLong("id"))
                .setUrl(rs.getString("url"));
    }
}
