package ru.tinkoff.edu.java.scrapper.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatLinkMapper implements RowMapper<ChatLink> {
    @Override
    public ChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatLink(rs.getLong("chat_id"), rs.getLong("link_id"));
    }
}
