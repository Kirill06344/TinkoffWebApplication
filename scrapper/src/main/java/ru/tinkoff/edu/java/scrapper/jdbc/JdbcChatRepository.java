package ru.tinkoff.edu.java.scrapper.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.mappers.ChatRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatRepository implements ChatRepository {

    private static final String SQL_ADD_CHAT = "insert into chat(id) values(?) on conflict do nothing returning id";

    private static final String SQL_FIND_CHAT_BY_ID = "select * from chat where id = ?";

    private static final String SQL_FIND_ALL_CHATS = "select * from chat";

    private static final String SQL_DELETE_CHAT_BY_ID = "delete from chat where id = ?";


    private final JdbcTemplate jdbcTemplate;
    private final ChatRowMapper chatMapper;

    @Override
    public Optional<Chat> add(Chat entity) {
        if (entity == null) {
            return Optional.empty();
        }

        try {
            var res = jdbcTemplate.queryForObject(SQL_ADD_CHAT, chatMapper, entity.getId());
            log.info("Add chat with id:" + entity.getId());
            return Optional.ofNullable(res);
        } catch (DataAccessException ex) {
            log.error(ex.getMessage());
            return findChatById(entity.getId());
        }
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL_CHATS, chatMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_CHAT_BY_ID, id);
    }

    @Override
    public Optional<Chat> findChatById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CHAT_BY_ID, chatMapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
