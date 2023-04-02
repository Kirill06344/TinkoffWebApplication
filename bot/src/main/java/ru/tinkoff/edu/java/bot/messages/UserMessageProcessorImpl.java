package ru.tinkoff.edu.java.bot.messages;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.commands.*;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.util.List;
import java.util.Optional;

@Component
public class UserMessageProcessorImpl implements UserMessageProcessor {

    private final List<? extends Command> commands;

    private MessageSupplier supplier;

    public UserMessageProcessorImpl() {
        HelpCommand helpCommand = new HelpCommand("/help", "Show all available commands");
        commands = List.of(
                new ListCommand("/list", "Show all tracked links"),
                new TrackCommand("/track", "Start tracking link"),
                new UntrackCommand("/untrack", "Stop tracking link"),
                helpCommand
        );
        helpCommand.setCommands(commands);
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        var command = getCommandFromUpdate(update);
        return command.isEmpty() ? new UnknownCommand().handle(update, supplier) : command.get().handle(update, supplier);
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

    @Autowired
    public void setSupplier(MessageSupplier supplier) {
        this.supplier = supplier;
    }
}
