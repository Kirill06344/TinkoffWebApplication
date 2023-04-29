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

    private static final String INVALID_LINK = "test_link";

    private static final String FIRST_VALID_LINK = "https://github.com/Kirill06344/TinkoffWebApplication";

    private static final String SECOND_VALID_LINK = "https://github.com/Kirill06344/TinkoffWebApplication";


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
        AddLinkRequest request = new AddLinkRequest(FIRST_VALID_LINK);
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
        AddLinkRequest request = new AddLinkRequest(FIRST_VALID_LINK);
        Link link = service.add(1L, request);
        assertAll(
                () -> assertEquals(FIRST_VALID_LINK, link.getUrl()),
                () -> assertTrue(link.getChats().stream().anyMatch(c -> c.getId() == 1L))
        );
    }

    @Test
    @Transactional
    @Rollback
    void should_addLinkOnceForTrackingBySomeChats() {
        chatRepository.save(new Chat().setId(1L));
        chatRepository.save(new Chat().setId(2L));
        AddLinkRequest request = new AddLinkRequest(FIRST_VALID_LINK);
        Link link1 = service.add(1L, request);
        Link link2 = service.add(2L, request);
        assertEquals(link1.getId(), link2.getId());
        assertAll(
                () -> assertEquals(FIRST_VALID_LINK, link1.getUrl()),
                () -> assertEquals(FIRST_VALID_LINK, link2.getUrl()),
                () -> assertTrue(link2.getChats().stream().allMatch(c -> c.getId() == 1L || c.getId() == 2L))
        );
        var link = linkRepository.findById(link1.getId());
        assertThat(link).isNotEmpty();
        assertEquals(2, link.get().getChats().size());
    }

    @Test
    @Transactional
    @Rollback
    void should_returnOneTrackingLink() {
        chatRepository.save(new Chat().setId(1L));
        AddLinkRequest request = new AddLinkRequest(FIRST_VALID_LINK);
        Link link = service.add(1L, request);
        assertAll(
                () -> assertEquals(1, service.listAll(1L).size()),
                () -> assertEquals(link.getUrl(), service.listAll(1L).get(0).getUrl())
        );
    }

    @Test
    @Transactional
    @Rollback
    void should_returnSomeTrackingLink() {
        chatRepository.save(new Chat().setId(1L));
        AddLinkRequest firstRequest = new AddLinkRequest(FIRST_VALID_LINK);
        AddLinkRequest secondRequest = new AddLinkRequest(SECOND_VALID_LINK);

        Link firstLink = service.add(1L, firstRequest);
        Link secondLink = service.add(1L, secondRequest);
        assertAll(
                () -> assertEquals(2, service.listAll(1L).size()),
                () -> assertTrue(service.listAll(1L).stream().anyMatch(l -> l.getUrl().equals(firstLink.getUrl()))),
                () -> assertTrue(service.listAll(1L).stream().anyMatch(l -> l.getUrl().equals(secondLink.getUrl()))),
                () -> assertTrue(secondLink.getChats().stream().anyMatch(c -> c.getId() == 1L))
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
