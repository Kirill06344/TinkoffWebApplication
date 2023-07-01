package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.scheduler.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.scheduler.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.sender.UpdateSender;
import ru.tinkoff.edu.java.scrapper.services.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.services.jdbc.JdbcTgChatService;
import ru.tinkoff.edu.java.scrapper.services.LinkService;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfiguration {

    private final DSLContext context;

    @Bean
    public JooqLinkRepository linkRepository() {
        return new JooqLinkRepository(context);
    }

    @Bean
    public JooqChatRepository chatRepository() {
        return new JooqChatRepository(context);
    }

    @Bean
    public JooqChatLinkRepository chatLinkRepository() {
        return new JooqChatLinkRepository(context);
    }

    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        LinkManager manager
    ) {
        return new JdbcLinkService(linkRepository, chatLinkRepository, manager);
    }

    @Bean
    public TgChatService tgChatService(
        JooqChatRepository tgChatRepository
    ) {
        return new JdbcTgChatService(tgChatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        LinkManager manager,
        UpdateSender sender
    ) {
        return new JdbcLinkUpdater(linkRepository, chatLinkRepository, manager, sender);
    }

}
