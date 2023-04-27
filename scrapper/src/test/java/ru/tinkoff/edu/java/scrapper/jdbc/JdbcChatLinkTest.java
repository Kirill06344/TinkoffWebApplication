package ru.tinkoff.edu.java.scrapper.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql("/sql_scripts/add_data_for_chats_and_links.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class JdbcChatLinkTest extends IntegrationEnvironment {

    @Autowired
    ChatLinkRepository repository;

    private final ChatLink FIRST_TEST_CHAT_LINK = new ChatLink(1L, 1L);

    @Test
    void should_returnEmptyIfChatLinkDoesNotExist() {
        var chatLink = repository.findChatLink(FIRST_TEST_CHAT_LINK);
        assertThat(chatLink).isEmpty();
    }

    @Test
    @Sql("/sql_scripts/add_chat_link.sql")
    @Transactional
    @Rollback
    void should_returnNotEmptyIfExists() {
        var chatLink = repository.findChatLink(FIRST_TEST_CHAT_LINK);
        assertThat(chatLink).isNotEmpty();
        assertEquals(FIRST_TEST_CHAT_LINK, chatLink.get());
    }

    @Test
    void should_returnEmptyListIfTableIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @Sql("/sql_scripts/add_three_chat_links.sql")
    @Transactional
    @Rollback
    void should_returnThreeLinks() {
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    void should_addChatLink() {
        var res = repository.add(FIRST_TEST_CHAT_LINK);
        assertThat(res).isNotEmpty();
        assertEquals(FIRST_TEST_CHAT_LINK, res.get());
    }

    @Test
    @Transactional
    @Rollback
    void should_returnExistingEntityIfDuplicate() {
        var first = repository.add(FIRST_TEST_CHAT_LINK);
        var second = repository.add(FIRST_TEST_CHAT_LINK);
        assertThat(first).isNotEmpty();
        assertThat(second).isNotEmpty();
        assertAll(
                () -> assertEquals(first.get().getChatId(), second.get().getChatId()),
                () -> assertEquals(first.get().getLinkId(), second.get().getLinkId())
        );
    }

    @Test
    @Transactional
    @Rollback
    void should_deleteByChatIdCorrectlyWithOneRow() {
        var entity = repository.add(FIRST_TEST_CHAT_LINK);
        assertThat(entity).isNotEmpty();
        repository.deleteAllChatLinksByChatId(entity.get().getChatId());
        assertTrue(repository.findChatLink(entity.get()).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Transactional
    @Sql("/sql_scripts/add_chat_links_with_same_chat_id.sql")
    @Rollback
    void should_deleteByChatIdCorrectlyWithSomeRows() {
        repository.deleteAllChatLinksByChatId(1L);
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void should_deleteByLinkIdCorrectlyWithOneRow() {
        var entity = repository.add(FIRST_TEST_CHAT_LINK);
        assertThat(entity).isNotEmpty();
        repository.deleteAllChatLinksByLinkId(entity.get().getLinkId());
        assertTrue(repository.findChatLink(entity.get()).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Transactional
    @Sql("/sql_scripts/add_chat_links_with_same_link_id.sql")
    @Rollback
    void should_deleteByLinkIdCorrectlyWithSomeRows() {
        repository.deleteAllChatLinksByLinkId(3L);
        assertEquals(0, repository.findAll().size());
    }


    @Test
    void should_deleteByIdFromEmptyTableWithOutExceptions() {
        repository.deleteAllChatLinksByChatId(1L);
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
