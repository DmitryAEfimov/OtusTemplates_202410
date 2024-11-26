package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.game.behavior.role.fuel.TankerObject;

@RequiredArgsConstructor
public class BurnFuel implements Command {
    private final TankerObject tanker;

    @Override
    public void execute() {
        tanker.burnFuel();
    }
}
