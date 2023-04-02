package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.commands.*;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

@Configuration
@RequiredArgsConstructor
public class CommandConfiguration {

    private final MessageSupplier supplier;

    @Bean
    ListCommand listCommand() {
       return ListCommand.builder()
                .command("/list")
                .description("Show all tracked links")
                .supplier(supplier)
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
