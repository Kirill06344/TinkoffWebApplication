package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.clients.BotClientImpl;
import ru.tinkoff.edu.java.scrapper.sender.HttpUpdateSender;
import ru.tinkoff.edu.java.scrapper.sender.UpdateSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
public class BotClientConfiguration {

    @Bean
    BotClient botClient(@Value("${bot.host}") String baseUrl) {
        return new BotClientImpl(baseUrl);
    }

    @Bean
    UpdateSender sender(BotClient client) {
        return new HttpUpdateSender(client);
    }
}
