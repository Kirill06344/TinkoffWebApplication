package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcChatTest extends IntegrationEnvironment {

    @Autowired
    ChatRepository repository;

    private final long TEST_CHAT_ID = 1L;

    @Test
    void should_returnEmptyIfChatDoesNotExist() {
        var chat = repository.findChatById(TEST_CHAT_ID);
        assertThat(chat).isEmpty();
    }

    @Test
    @Sql(value = "/sql_scripts/add_chat.sql")
    @Transactional
    @Rollback
    void should_returnNotEmptyIfChatExists() {
        var chat = repository.findChatById(TEST_CHAT_ID);
        assertThat(chat).isNotEmpty();
        assertEquals(TEST_CHAT_ID, chat.get().getId());
    }

    @Test
    void should_returnEmptyListIfTableIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @Sql("/sql_scripts/add_three_chats.sql")
    @Transactional
    @Rollback
    void should_returnThreeChats() {
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    void should_addChat() {
        var res = repository.add(new Chat(TEST_CHAT_ID));
        assertThat(res).isNotEmpty();
        assertEquals(TEST_CHAT_ID, res.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    void should_returnExistingEntityIfDuplicate() {
        var first = repository.add(new Chat(TEST_CHAT_ID));
        var second = repository.add(new Chat(TEST_CHAT_ID));
        assertThat(first).isNotEmpty();
        assertThat(second).isNotEmpty();
        assertEquals(first.get().getId(), second.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    void should_deleteByIdCorrectly() {
        var entity = repository.add(new Chat(TEST_CHAT_ID));
        assertThat(entity).isNotEmpty();
        repository.deleteById(entity.get().getId());
        assertTrue(repository.findChatById(TEST_CHAT_ID).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Transactional
    @Sql("/sql_scripts/add_three_chats.sql")
    @Rollback
    void should_deleteByIdCorrectlyWithSomeRows() {
        long id = 123L;
        repository.deleteById(id);
        assertTrue(repository.findChatById(id).isEmpty());
        var chats = repository.findAll();
        assertEquals(2, chats.size());
        assertFalse(chats.contains(new Chat(123L)));
    }

    @Test
    void should_deleteByIdFromEmptyTableWithOutExceptions() {
        repository.deleteById(1L);
        assertEquals(0, repository.findAll().size());
    }


    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", MY_POSTGRES_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", MY_POSTGRES_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MY_POSTGRES_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", MY_POSTGRES_CONTAINER::getDriverClassName);
        dynamicPropertyRegistry.add("spring.liquibase.enabled", () -> Boolean.FALSE);
    }

}
