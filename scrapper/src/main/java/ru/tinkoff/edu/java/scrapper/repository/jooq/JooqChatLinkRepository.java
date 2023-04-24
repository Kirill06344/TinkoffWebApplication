package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.ChatLinkRecord;
import ru.tinkoff.edu.java.scrapper.model.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;

@Repository
@Primary
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext context;

    @Autowired
    public JooqChatLinkRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<ChatLink> findAllChatLinksByChatId(long chatId) {
        Result<ChatLinkRecord> result = context.selectFrom(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq(chatId)).fetch();
        List<ChatLink> chats = new ArrayList<>();
        for (ChatLinkRecord r : result) {
            chats.add(new ChatLink(r.getChatId(), r.getLinkId()));
        }
        return chats;
    }

    @Override
    public List<ChatLink> findAllChatLinksByLinkId(long linkId) {
        Result<ChatLinkRecord> result = context.selectFrom(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId)).fetch();
        List<ChatLink> chats = new ArrayList<>();
        for (ChatLinkRecord r : result) {
            chats.add(new ChatLink(r.getChatId(), r.getLinkId()));
        }
        return chats;
    }

    @Override
    public Optional<ChatLink> findChatLink(ChatLink entity) {
        ChatLinkRecord chatLink = context.selectFrom(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(entity.getLinkId()))
                .and(CHAT_LINK.CHAT_ID.eq(entity.getChatId()))
                .fetchOne();
        return (chatLink != null) ? Optional.of(new ChatLink(chatLink.getChatId(), chatLink.getLinkId())) : Optional.empty();
    }

    @Override
    public int deleteAllChatLinksByChatId(long chatId) {
        return context.deleteFrom(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq(chatId)).execute();
    }

    @Override
    public int deleteAllChatLinksByLinkId(long linkId) {
        return context.deleteFrom(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId)).execute();
    }

    @Override
    public Optional<ChatLink> add(ChatLink entity) {
        if (entity == null) {
            return Optional.empty();
        }

        ChatLinkRecord record = context.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
                .values(entity.getChatId(), entity.getLinkId())
                .onDuplicateKeyIgnore()
                .returning(CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
                .fetchOne();

        return (record != null) ? Optional.of(new ChatLink(record.getChatId(), record.getLinkId())) : findChatLink(entity);
    }

    @Override
    public List<ChatLink> findAll() {
        Result<ChatLinkRecord> result = context.selectFrom(CHAT_LINK).fetch();
        List<ChatLink> chats = new ArrayList<>();
        for (ChatLinkRecord r : result) {
            chats.add(new ChatLink(r.getChatId(), r.getLinkId()));
        }
        return chats;
    }

    @Override
    public int deleteById(ChatLink id) {
        return context.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(id.getChatId()))
                .and(CHAT_LINK.LINK_ID.eq(id.getLinkId()))
                .execute();
    }
}
