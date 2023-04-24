package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.model.ChatLink;

import java.util.List;
import java.util.Optional;

public interface ChatLinkRepository extends Repository<ChatLink, ChatLink>{

    List<ChatLink> findAllChatLinksByChatId(long chatId);

    List<ChatLink> findAllChatLinksByLinkId(long linkId);

    Optional<ChatLink> findChatLink(ChatLink entity);

    int deleteAllChatLinksByChatId(long chatId);

    int deleteAllChatLinksByLinkId(long linkId);

}
