package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface Message {
    SendMessage handle(Update update, MessageSupplier supplier);

    default long getChatId(Update update) {
        return update.message().chat().id();
    }

    default String readFile(String path, Charset encoding) throws IOException {
        return Files.readString(Paths.get(path), encoding);
    }
}
