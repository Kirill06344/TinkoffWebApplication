package ru.tinkoff.edu.java.scrapper.jpa;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JpaTgChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private TgChatService service;

    @Autowired
    private JpaChatRepository repository;

    @Test
    @Transactional
    @Rollback
    void should_registerChatSuccessfully() {
        service.register(1L);
        var optChat = repository.findById(1L);
        assertThat(optChat).isNotEmpty();
        Chat chat = optChat.get();
        assertAll(
                () -> assertEquals(1L, chat.getId()),
                () -> assertEquals(1, repository.findAll().size())
        );
    }

    @Test
    @Transactional
    @Rollback
    void should_notRegisterChatIfItAlreadyInTheTable() {
        service.register(1L);
        service.register(1L);
        assertEquals(1, repository.count());
        var optChat = repository.findById(1L);
        assertThat(optChat).isNotEmpty();
        Chat chat = optChat.get();
        assertEquals(1L, chat.getId());
    }

    @Test
    @Sql("/sql_scripts/add_three_chats.sql")
    @Transactional
    @Rollback
    void should_deleteByIdCorrectly() {
        assertEquals(3, repository.count());
        assertEquals(1, service.unregister(4L));
        assertEquals(2, repository.count());
    }

    @Test
    @Sql("/sql_scripts/add_three_chats.sql")
    @Transactional
    @Rollback
    void should_NotDeleteByIdNotExistingChat() {
        assertEquals(3, repository.count());
        assertEquals(0, service.unregister(3224442L));
        assertEquals(3, repository.count());
    }

    @Test
    @Transactional
    @Rollback
    void should_RegisterAndUnregisterSuccessfully() {
        service.register(1L);
        service.register(2L);
        assertEquals(2, repository.count());
        service.unregister(1L);
        service.unregister(2L);
        assertEquals(0, repository.count());
    }


    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", MY_POSTGRES_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", MY_POSTGRES_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MY_POSTGRES_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", MY_POSTGRES_CONTAINER::getDriverClassName);
        dynamicPropertyRegistry.add("spring.liquibase.enabled", () -> Boolean.FALSE);
        dynamicPropertyRegistry.add("app.database-access-type", () -> "jpa");
    }
}
