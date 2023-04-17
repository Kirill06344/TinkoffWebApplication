package ru.tinkoff.edu.java.scrapper.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.mappers.ChatLinkMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private static final String SQL_FIND_CHAT_LINK_BY_CHAT_ID = "select * from chat_link where chat_id = ?";
    private static final String SQL_FIND_CHAT_LINK_BY_LINK_ID = "select * from chat_link where chat_id = ?";
    private static final String SQL_DELETE_ALL_CHAT_LINKS_BY_CHAT_ID = "delete from chat_link where chat_id = ?";
    private static final String SQL_DELETE_ALL_CHAT_LINKS_BY_LINK_ID = "delete from chat_link where link_id = ?";
    private static final String SQL_DELETE_CHAT_LINK_BY_CHAT_LINK = "delete from chat_link where chat_id = ? and link_id = ?";

    private static final String SQL_FIND_CHAT_LINK = "select * from chat_link where chat_id = ? and link_id = ?";
    private static final String SQL_ADD_CHAT_LINK =
            "insert into chat_link(chat_id, link_id) values(?, ?) on conflict do nothing returning chat_id,link_id";

    private static final String SQL_FIND_ALL_CHAT_LINK = "select * from chat_link";


    private final JdbcTemplate jdbcTemplate;

    private final ChatLinkMapper chatLinkMapper;

    @Override
    public List<ChatLink> findAllChatLinksByChatId(long chatId) {
        return jdbcTemplate.query(SQL_FIND_CHAT_LINK_BY_CHAT_ID, chatLinkMapper, chatId);
    }

    @Override
    public List<ChatLink> findAllChatLinksByLinkId(long linkId) {
        return jdbcTemplate.query(SQL_FIND_CHAT_LINK_BY_LINK_ID, chatLinkMapper, linkId);
    }

    @Override
    public Optional<ChatLink> findChatLink(ChatLink chatLink) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CHAT_LINK, chatLinkMapper,
                    chatLink.getChatId(), chatLink.getLinkId()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAllChatLinksByChatId(long chatId) {
        jdbcTemplate.update(SQL_DELETE_ALL_CHAT_LINKS_BY_CHAT_ID, chatId);
    }

    @Override
    public void deleteAllChatLinksByLinkId(long linkId) {
        jdbcTemplate.update(SQL_DELETE_ALL_CHAT_LINKS_BY_LINK_ID, linkId);
    }

    @Override
    public Optional<ChatLink> add(ChatLink entity) {
        if (entity == null) {
            return Optional.empty();
        }

        try {
            var res = jdbcTemplate.queryForObject(SQL_ADD_CHAT_LINK, chatLinkMapper, entity.getChatId(), entity.getLinkId());
            return Optional.ofNullable(res);
        } catch (DataAccessException ex) {
            return findChatLink(entity);
        }
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL_CHAT_LINK, chatLinkMapper);
    }

    @Override
    public void deleteById(ChatLink id) {
        jdbcTemplate.update(SQL_DELETE_CHAT_LINK_BY_CHAT_LINK, id.getChatId(), id.getLinkId());
    }
}
