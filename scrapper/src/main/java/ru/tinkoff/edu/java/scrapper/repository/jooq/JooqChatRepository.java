package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT;

@Repository
public class JooqChatRepository implements ChatRepository {

    private final DataSource dataSource;

    private final DSLContext context;

    @Autowired
    public JooqChatRepository(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        context = DSL.using(dataSource.getConnection(), SQLDialect.POSTGRES);
    }


    @Override
    public Optional<Chat> findChatById(long id) {
        Result<Record> result =  context.select().from(CHAT).where(CHAT.ID.eq(id)).fetch();
        return result.isEmpty() ? Optional.empty() : Optional.of(new Chat(result.get(0).getValue(CHAT.ID)));
    }

    @Override
    public Optional<Chat> add(Chat entity) {
        Record record = context.insertInto(CHAT, CHAT.ID).values(entity.getId()).returning(CHAT.ID).fetchOne();
        return (record == null || record.get(CHAT.ID) == null) ? Optional.empty()
                : Optional.of(new Chat(record.get(CHAT.ID)));
    }

    @Override
    public List<Chat> findAll() {
        Result<Record> result =  context.select().from(CHAT).fetch();
        List<Chat> chats = new ArrayList<>();
        for (Record r : result) {
            chats.add(new Chat(r.getValue(CHAT.ID)));
        }
        return chats;
    }

    @Override
    public int deleteById(Long id) {
        return context.delete(CHAT).where(CHAT.ID.eq(id)).execute();
    }
}
