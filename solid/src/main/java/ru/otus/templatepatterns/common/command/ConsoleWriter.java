package ru.otus.templatepatterns.common.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsoleWriter implements Command {
    private static final String EXCEPTION_MSG_TEMPLATE = "No exception handler registered for [exception: %s, command: %s]";

    private final Command command;
    private final Exception exception;

    @Override
    public void execute() {
        System.out.printf(EXCEPTION_MSG_TEMPLATE, exception.getClass().getName(), command.getClass().getName());
    }
}
