package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.game.behavior.attributes.Angle;
import ru.otus.templatepatterns.game.behavior.attributes.Vector;
import ru.otus.templatepatterns.game.behavior.role.moving.ChangingVelocityObject;

@RequiredArgsConstructor
public class ChangeVelocity implements Command {
    private final ChangingVelocityObject changingVelocity;
    private final Vector velocity;
    private final Angle movingAngle;

    @Override
    public void execute() {
        changingVelocity.setVelocity(velocity);
        changingVelocity.setAngle(movingAngle);
    }
}
