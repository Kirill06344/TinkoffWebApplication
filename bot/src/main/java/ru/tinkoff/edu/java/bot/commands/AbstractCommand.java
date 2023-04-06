package ru.tinkoff.edu.java.bot.commands;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class AbstractCommand implements Command {

    protected String command;

    protected String description;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return description;
    }

}
