package ru.tinkoff.edu.java.scrapper.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests annotated with @DataJpaTest are transactional and roll back at the end of each test.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaChatTest extends IntegrationEnvironment {

    @Autowired
    private  JpaChatRepository repository;

    private final long TEST_CHAT_ID = 1L;

    @Test
    void should_returnEmptyIfChatDoesNotExist(){
        assertThat(repository.findById(TEST_CHAT_ID)).isEmpty();
    }

    @Test
    @Sql(value = "/sql_scripts/add_chat.sql")
    void should_returnNotEmptyIfChatExists() {
        var chat = repository.findById(TEST_CHAT_ID);
        assertThat(chat).isNotEmpty();
        assertEquals(TEST_CHAT_ID, chat.get().getId());
    }

    @Test
    void should_returnEmptyListIfTableIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @Sql("/sql_scripts/add_three_chats.sql")
    void should_returnThreeChats() {
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    void should_addChat() {
        System.out.println(repository.findAll().size());
        var res = repository.save(new Chat().setId(TEST_CHAT_ID));
        assertNotNull(res);
        assertEquals(TEST_CHAT_ID, res.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void should_returnExistingEntityIfDuplicate() {
        Chat first = repository.save(new Chat().setId(TEST_CHAT_ID));
        Chat second = repository.save(new Chat().setId(TEST_CHAT_ID));
        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.getId(), second.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void should_deleteByIdCorrectly() {
        Chat chat = repository.save(new Chat().setId(TEST_CHAT_ID));
        assertNotNull(chat);
        assertEquals(1, repository.deleteById(TEST_CHAT_ID));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Sql("/sql_scripts/add_three_chats.sql")
    void should_deleteByIdCorrectlyWithSomeRows() {
        assertEquals(3, repository.findAll().size());
        long id = 123L;
        assertEquals(1,repository.deleteById(id));
        var chats = repository.findAll();
        assertEquals(2, chats.size());
        assertFalse(chats.contains(new Chat().setId(123L)));
    }

    @Test
    void should_deleteByIdFromEmptyTableWithOutExceptions() {
        assertEquals(0, repository.deleteById(1L));
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
