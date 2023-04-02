package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Slf4j
public class HelpCommand extends AbstractCommand {

    private List<? extends Command> commands;
    public HelpCommand(String command, String description) {
        super(command, description);
    }

    @Override
    public SendMessage handle(Update update, MessageSupplier supplier) {
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("commands", commands);
        return new SendMessage(getChatId(update),
                supplier.convertTemplate("help.mustache", scopes))
                .parseMode(ParseMode.HTML);
    }
}
