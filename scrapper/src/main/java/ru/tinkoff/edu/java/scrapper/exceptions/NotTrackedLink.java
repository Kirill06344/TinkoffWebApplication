package ru.tinkoff.edu.java.scrapper.exceptions;

public class NotTrackedLink extends RuntimeException{

    private String message = "You have not track this link already:\n %s";

    public NotTrackedLink(String link) {
        message = message.formatted(link);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
