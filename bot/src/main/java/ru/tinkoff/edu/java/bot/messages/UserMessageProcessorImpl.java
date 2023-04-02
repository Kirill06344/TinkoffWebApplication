package ru.tinkoff.edu.java.bot.messages;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.commands.*;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMessageProcessorImpl implements UserMessageProcessor {

    private final List<? extends Command> commands;

    private final MessageSupplier supplier;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        var command = getCommandFromUpdate(update);
        return command.isEmpty() ? new UnknownCommand(supplier).handle(update) : command.get().handle(update);
    }

    public BotCommand[] botCommands() {
        return commands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new);
    }

    public Optional<? extends Command> getCommandFromUpdate(Update update) {
        return commands().stream()
                .filter(c -> c.supports(update))
                .findFirst();
    }

}
