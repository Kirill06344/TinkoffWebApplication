package ru.tinkoff.edu.java.bot.commands;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCommand implements Command {

    protected final String command;
    protected final String description;
    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return description;
    }
}
