package ru.tinkoff.edu.java.bot.utils;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class TrackingCommandValidator {

    public boolean supports(Update update, String command) {
       String regex = command + " *";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(update.message().text()).find();
    }

    public Optional<String> getLink(Update update) {
        String message = update.message().text();
        String [] parts = message.trim().split(" ");
        return parts.length == 2 ? Optional.of(parts[1]) : Optional.empty();
    }


}
