package ru.tinkoff.edu.java.bot.utils;

import java.util.Map;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public interface MessageSender {

    String convertTemplate(String path, Map<String, Object> scopes);

    SendMessage send(long id, String response, Map<String, Object> scopes);

}
