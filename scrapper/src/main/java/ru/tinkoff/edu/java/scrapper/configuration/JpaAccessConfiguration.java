package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.scheduler.JpaLinkUpdater;
import ru.tinkoff.edu.java.scrapper.scheduler.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.services.*;
import ru.tinkoff.edu.java.scrapper.services.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.services.jpa.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.utils.LinkManager;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
            JpaLinkRepository linkRepository,
            JpaChatRepository chatRepository,
            LinkManager manager
    ) {
        return new JpaLinkService(linkRepository, chatRepository, manager);
    }

    @Bean
    public TgChatService tgChatService(
            JpaChatRepository chatRepository
    ) {
        return new JpaTgChatService(chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JpaLinkRepository linkRepository,
            LinkManager manager,
            BotClient botClient
    ) {
        return new JpaLinkUpdater(linkRepository,manager, botClient);
    }
}
