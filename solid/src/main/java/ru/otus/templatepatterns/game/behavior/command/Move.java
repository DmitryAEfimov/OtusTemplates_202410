package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.game.behavior.attributes.Vector;
import ru.otus.templatepatterns.game.behavior.role.moving.MovingObject;

@RequiredArgsConstructor
public class Move implements Command {
    private final MovingObject moving;

    @Override
    public void execute() {
        moving.setPosition(Vector.plus(moving.getPosition(), moving.getVelocity()));
    }
}
