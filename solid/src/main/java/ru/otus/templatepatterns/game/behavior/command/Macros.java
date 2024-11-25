package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.common.command.Command;

import java.util.List;

@RequiredArgsConstructor
public class Macros implements Command {
    private final List<Command> commands;

    @Override
    public void execute() {
        commands.forEach(Command::execute);
    }
}
