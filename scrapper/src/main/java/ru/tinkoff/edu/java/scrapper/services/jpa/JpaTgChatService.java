package ru.tinkoff.edu.java.scrapper.services.jpa;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.services.TgChatService;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaChatRepository chatRepository;

    public void register(long tgChatId) {
        Chat chat = new Chat().setId(tgChatId);
        chatRepository.save(chat);
    }

    @Override
    public int unregister(long tgChatId) {
        return chatRepository.deleteById(tgChatId);
    }
}
