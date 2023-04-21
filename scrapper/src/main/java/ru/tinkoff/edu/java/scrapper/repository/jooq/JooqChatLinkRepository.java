package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;

@Repository
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DataSource dataSource;

    private final DSLContext context;

    @Autowired
    public JooqChatLinkRepository(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        context = DSL.using(dataSource.getConnection(), SQLDialect.POSTGRES);
    }

    @Override
    public List<ChatLink> findAllChatLinksByChatId(long chatId) {
        Result<Record> result =  context.select().from(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq(chatId)).fetch();
        List<ChatLink> chats = new ArrayList<>();
        for (Record r : result) {
            chats.add(new ChatLink(r.getValue(CHAT_LINK.CHAT_ID), r.getValue(CHAT_LINK.LINK_ID)));
        }
        return chats;
    }

    @Override
    public List<ChatLink> findAllChatLinksByLinkId(long linkId) {
        Result<Record> result =  context.select().from(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId)).fetch();
        List<ChatLink> chats = new ArrayList<>();
        for (Record r : result) {
            chats.add(new ChatLink(r.getValue(CHAT_LINK.CHAT_ID), r.getValue(CHAT_LINK.LINK_ID)));
        }
        return chats;
    }

    @Override
    public Optional<ChatLink> findChatLink(ChatLink entity) {
        return Optional.empty();
        //context.select().from(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(entity.getLinkId())).and(CHAT_LINK.CHAT_ID.eq(entity.getChatId()))
    }

    @Override
    public int deleteAllChatLinksByChatId(long chatId) {
        return 0;
    }

    @Override
    public int deleteAllChatLinksByLinkId(long linkId) {
        return 0;
    }

    @Override
    public Optional<ChatLink> add(ChatLink entity) {
        return Optional.empty();
    }

    @Override
    public List<ChatLink> findAll() {
        return null;
    }

    @Override
    public int deleteById(ChatLink id) {
        return 0;
    }
}
