package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.UpdateHandler;
import ru.tinkoff.edu.java.bot.messages.UserMessageProcessorImpl;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfiguration {

    private final TelegramBotProperties properties;

    private final UserMessageProcessorImpl messageProcessor;

    @Bean
    TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(properties.getToken());
        UpdateHandler servlet = new UpdateHandler(bot, messageProcessor);
        bot.setUpdatesListener(servlet);
        bot.execute(new SetMyCommands(messageProcessor.botCommands()));
        return bot;
    }


}
