package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.entity.ChatLink;

import java.util.List;
import java.util.Optional;

public interface ChatLinkRepository extends Repository<ChatLink, ChatLink>{

    List<ChatLink> findAllChatLinksByChatId(long chatId);

    List<ChatLink> findAllChatLinksByLinkId(long linkId);

    Optional<ChatLink> findChatLink(ChatLink entity);

    void deleteAllChatLinksByChatId(long chatId);

    void deleteAllChatLinksByLinkId(long linkId);

}
