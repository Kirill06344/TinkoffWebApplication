package ru.tinkoff.edu.java.scrapper.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidLink;
import ru.tinkoff.edu.java.scrapper.exceptions.NotExistingChat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.services.LinkService;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class JpaLinkServiceTest extends IntegrationEnvironment {

    @Autowired
    private LinkService service;

    @Autowired
    private JpaChatRepository chatRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

    private final String INVALID_LINK = "test_link";

    private final String VALID_LINK = "https://github.com/Kirill06344/TinkoffWebApplication";

    @Test
    void should_throwErrorIfInvalidLink() {
        chatRepository.save(new Chat().setId(1L));
        AddLinkRequest request = new AddLinkRequest(INVALID_LINK);
        try {
            service.add(1L, request);
        } catch (InvalidLink ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    void should_throwErrorIfChatNotExisting() {
        AddLinkRequest request = new AddLinkRequest(VALID_LINK);
        try {
            service.add(1L, request);
        } catch (NotExistingChat ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    void should_addExistingLink() {
        chatRepository.save(new Chat().setId(1L));
        AddLinkRequest request = new AddLinkRequest(VALID_LINK);
        Link link = service.add(1L, request);
        assertAll(
                () -> assertEquals(VALID_LINK, link.getUrl()),
                () -> assertTrue(link.getChats().stream().anyMatch(c -> c.getId() == 1L))
        );
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
