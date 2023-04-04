package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.commands.*;
import ru.tinkoff.edu.java.bot.service.ScrapperService;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;
import ru.tinkoff.edu.java.bot.utils.TrackingCommandValidator;

@Configuration
@RequiredArgsConstructor
public class CommandConfiguration {

    private final MessageSupplier supplier;

    private final ScrapperService service;

    private final TrackingCommandValidator validator;

    @Bean
    ListCommand listCommand() {
        return ListCommand.builder()
                .command("/list")
                .description("Show all tracked links")
                .supplier(supplier)
                .service(service)
                .build();
    }

    @Bean
    HelpCommand helpCommand() {
        return HelpCommand.builder()
                .command("/help")
                .description("Show all available commands")
                .supplier(supplier)
                .build();
    }

    @Bean
    TrackCommand trackCommand() {
        return TrackCommand.builder()
                .command("/track")
                .description("Start tracking link")
                .supplier(supplier)
                .service(service)
                .validator(validator)
                .build();
    }

    @Bean
    UntrackCommand untrackCommand() {
        return UntrackCommand.builder().command("/untrack")
                .description("Stop tracking link")
                .supplier(supplier)
                .build();
    }

}