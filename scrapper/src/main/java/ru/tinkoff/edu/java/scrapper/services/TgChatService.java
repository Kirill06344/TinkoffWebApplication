package ru.tinkoff.edu.java.scrapper.services;

public interface TgChatService {
    void register(long tgChatId);
    int unregister(long tgChatId);
}
