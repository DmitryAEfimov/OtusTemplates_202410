package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.common.exception.NoFuelException;
import ru.otus.templatepatterns.game.behavior.role.fuel.CheckFuelObject;

@RequiredArgsConstructor
public class CheckFuel implements Command {
    private final CheckFuelObject checkFuelObject;

    @Override
    public void execute() {
        if (!checkFuelObject.isFuelEnough(checkFuelObject.getInstantConsumption())) {
            throw new NoFuelException();
        }
    }
}
