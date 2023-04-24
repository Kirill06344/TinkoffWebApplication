package ru.tinkoff.edu.java.scrapper.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ChatLink {
    private final long chatId;
    private final long linkId;
}
