package ru.otus.templatepatterns.common.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Repeat implements Command {
    private final Command command;
    private final Exception exception;

    @Override
    public void execute() {
        command.execute();
    }
}
