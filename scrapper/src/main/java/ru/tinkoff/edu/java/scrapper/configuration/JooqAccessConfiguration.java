package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.scheduler.JooqLinkUpdater;
import ru.tinkoff.edu.java.scrapper.scheduler.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.services.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.services.JooqTgChatService;
import ru.tinkoff.edu.java.scrapper.services.LinkService;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public LinkService linkService(
            JooqLinkRepository linkRepository,
            JooqChatLinkRepository chatLinkRepository,
            LinkManager manager
    ) {
        return new JooqLinkService(linkRepository, chatLinkRepository, manager);
    }

    @Bean
    public TgChatService tgChatService(
            JooqChatRepository tgChatRepository
    ) {
        return new JooqTgChatService(tgChatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JooqLinkRepository linkRepository,
            JooqChatLinkRepository chatLinkRepository,
            LinkManager manager,
            BotClient botClient
    ) {
        return new JooqLinkUpdater(linkRepository, chatLinkRepository, manager, botClient);
    }

}
