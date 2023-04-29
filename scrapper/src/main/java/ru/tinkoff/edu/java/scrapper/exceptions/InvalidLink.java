package ru.tinkoff.edu.java.scrapper.exceptions;

public class InvalidLink extends RuntimeException{
    private String message = "You try to track not existing link: %s";

    public InvalidLink(String link) {
        message = message.formatted(link);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
