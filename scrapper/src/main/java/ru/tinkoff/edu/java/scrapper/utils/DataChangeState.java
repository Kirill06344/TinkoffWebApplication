package ru.tinkoff.edu.java.scrapper.utils;

public enum DataChangeState {
    COUNT("count"),
    OTHER("other"),
    NOTHING("nothing");

    private String name;

    DataChangeState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
