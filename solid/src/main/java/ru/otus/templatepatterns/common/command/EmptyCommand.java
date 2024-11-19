package ru.otus.templatepatterns.common.command;

public class EmptyCommand implements Command {
    @Override
    public void execute() {
        // do nothing
    }
}
