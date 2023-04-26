package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.scheduler.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.scheduler.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.services.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.services.JdbcTgChatService;
import ru.tinkoff.edu.java.scrapper.services.LinkService;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
            JdbcLinkRepository linkRepository,
            JdbcChatLinkRepository chatLinkRepository,
            LinkManager manager
    ) {
        return new JdbcLinkService(linkRepository, chatLinkRepository, manager);
    }

    @Bean
    public TgChatService tgChatService(
            JdbcChatRepository tgChatRepository
    ) {
        return new JdbcTgChatService(tgChatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JdbcLinkRepository linkRepository,
            JdbcChatLinkRepository chatLinkRepository,
            LinkManager manager,
            BotClient botClient
    ) {
        return new JdbcLinkUpdater(linkRepository, chatLinkRepository, manager, botClient);
    }

}
