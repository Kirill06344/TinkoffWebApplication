package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    LinkRepository repository;

    private final String TEST_URL = "https://stackoverflow.com/questions/22234";

    @Test
    void should_returnEmptyIfLinkDoesNotExist() {
        var link = repository.findLinkByUrl(TEST_URL);
        assertThat(link).isEmpty();
    }

    @Test
    @Sql(value = "/sql_scripts/add_link.sql")
    @Transactional
    @Rollback
    void should_returnNotEmptyIfExists() {
        var link = repository.findLinkByUrl(TEST_URL);
        assertThat(link).isNotEmpty();
        assertEquals(TEST_URL, link.get().getUrl());
    }

    @Test
    void should_returnEmptyListIfTableIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @Sql("/sql_scripts/add_three_links.sql")
    @Transactional
    @Rollback
    void should_returnThreeLinks() {
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    void should_addLink() {
        var res = repository.add(new Link().setUrl(TEST_URL));
        assertThat(res).isNotEmpty();
        assertEquals(TEST_URL, res.get().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void should_returnExistingEntityIfDuplicate() {
        var first = repository.add(new Link().setUrl(TEST_URL));
        var second = repository.add(new Link().setUrl(TEST_URL));
        assertThat(first).isNotEmpty();
        assertThat(second).isNotEmpty();
        assertEquals(first.get(), second.get());
    }

    @Test
    @Transactional
    @Rollback
    void should_deleteByIdCorrectly() {
        var entity = repository.add(new Link().setUrl(TEST_URL));
        assertThat(entity).isNotEmpty();
        repository.deleteById(entity.get().getId());
        assertTrue(repository.findLinkByUrl(TEST_URL).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void should_deleteByIdFromEmptyTableWithOutExceptions() {
        repository.deleteById(1L);
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Transactional
    @Sql("/sql_scripts/add_three_links.sql")
    @Rollback
    void should_deleteByUrlCorrectly() {
        String url = "https://stackoverflow.com/questions/77454";
        repository.deleteLinkByUrl(url);
        assertThat(repository.findLinkByUrl(url)).isEmpty();
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void should_deleteByUrlFromEmptyTableWithOutExceptions() {
        String url = "https://stackoverflow.com/questions/77454";
        repository.deleteLinkByUrl(url);
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
