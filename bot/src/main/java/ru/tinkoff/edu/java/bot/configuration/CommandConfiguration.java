package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.commands.*;
import ru.tinkoff.edu.java.bot.utils.MessageSender;
import ru.tinkoff.edu.java.bot.utils.TrackingCommandValidator;

@Configuration
@RequiredArgsConstructor
public class CommandConfiguration {

    private final MessageSender sender;

    private final ScrapperClient client;
    private final TrackingCommandValidator validator;

    @Bean
    StartCommand startCommand() {
        return StartCommand.builder()
            .command("/start")
            .description("Register your id at the system to tracking")
            .client(client)
            .sender(sender)
            .build();
    }

    @Bean
    ListCommand listCommand() {
        return ListCommand.builder()
            .command("/list")
            .description("Show all tracked links")
            .sender(sender)
            .client(client)
            .build();
    }

    @Bean
    HelpCommand helpCommand() {
        return HelpCommand.builder()
            .command("/help")
            .description("Show all available commands")
            .sender(sender)
            .build();
    }

    @Bean
    TrackCommand trackCommand() {
        return TrackCommand.builder()
            .command("/track")
            .description("Start tracking link")
            .sender(sender)
            .client(client)
            .validator(validator)
            .build();
    }

    @Bean
    UntrackCommand untrackCommand() {
        return UntrackCommand.builder().command("/untrack")
            .description("Stop tracking link")
            .sender(sender)
            .client(client)
            .validator(validator)
            .build();
    }

}
