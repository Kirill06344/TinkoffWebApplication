package ru.tinkoff.edu.java.scrapper.exceptions;

public class NotExistingChat extends RuntimeException {

    private String message = "This chat with id %o doesn't exist!";

    public NotExistingChat(long chat) {
        message = message.formatted(chat);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
