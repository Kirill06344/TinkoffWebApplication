package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.clients.BotClient;
import ru.tinkoff.edu.java.scrapper.clients.BotClientImpl;

@Configuration
public class BotClientConfiguration {

    @Bean
    BotClient botClient(@Value("${bot.host}") String baseUrl) {
        return new BotClientImpl(baseUrl);
    }
}