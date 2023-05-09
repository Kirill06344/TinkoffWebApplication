package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT;

public class JooqChatRepository implements ChatRepository {

    private final DSLContext context;

    public JooqChatRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<Chat> findChatById(long id) {
        Result<Record> result = context.select().from(CHAT).where(CHAT.ID.eq(id)).fetch();
        return result.isEmpty() ? Optional.empty() : Optional.of(new Chat().setId(result.get(0).getValue(CHAT.ID)));
    }

    @Override
    public Optional<Chat> add(Chat entity) {
        Record record =
            context.insertInto(CHAT, CHAT.ID).values(entity.getId()).onDuplicateKeyIgnore().returning(CHAT.ID)
                .fetchOne();
        return (record == null || record.get(CHAT.ID) == null) ? findChatById(entity.getId())
            : Optional.of(new Chat().setId(record.get(CHAT.ID)));
    }

    @Override
    public List<Chat> findAll() {
        Result<Record> result = context.select().from(CHAT).fetch();
        List<Chat> chats = new ArrayList<>();
        for (Record r : result) {
            chats.add(new Chat().setId(r.getValue(CHAT.ID)));
        }
        return chats;
    }

    @Override
    public int deleteById(Long id) {
        return context.delete(CHAT).where(CHAT.ID.eq(id)).execute();
    }
}
