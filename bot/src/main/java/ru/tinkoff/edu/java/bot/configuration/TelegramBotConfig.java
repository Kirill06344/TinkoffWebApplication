package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.commands.Command;
import ru.tinkoff.edu.java.bot.messages.UserMessageProcessorImpl;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {


    private final TelegramBotProperties properties;

    private final UserMessageProcessorImpl messageProcessor;
    @Bean
    TelegramBot telegramBot() {
        System.out.println(properties.getToken());
        TelegramBot bot = new TelegramBot(properties.getToken());
        bot.execute(new SetMyCommands(messageProcessor.botCommands()));
        return bot;
    }

}
