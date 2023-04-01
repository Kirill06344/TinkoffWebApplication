package ru.tinkoff.edu.java.bot.messages;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.commands.Command;
import ru.tinkoff.edu.java.bot.commands.HelpCommand;

import java.util.List;

@Component
public class UserMessageProcessorImpl implements UserMessageProcessor {
    @Override
    public List<? extends Command> commands() {
        return List.of(new HelpCommand());
    }

    @Override
    public SendMessage process(Update update) {
        return null;
    }

    public BotCommand[] botCommands() {
        return commands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new);
    }
}
