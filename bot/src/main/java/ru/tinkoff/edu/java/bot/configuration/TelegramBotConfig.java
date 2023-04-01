package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {


    private final TelegramBotProperties properties;
    @Bean
    TelegramBot telegramBot() {
        System.out.println(properties.getToken());
        return new TelegramBot(properties.getToken());
    }

}
