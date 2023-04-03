package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.clients.ScrapperClientImpl;

@Configuration
public class ScrapperClientConfiguration {
    @Bean
    ScrapperClient scrapperClient(@Value("${scrapper.host}") String baseUrl) {
        return new ScrapperClientImpl(baseUrl);
    }
}
