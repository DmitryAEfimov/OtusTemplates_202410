package ru.otus.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.game.behavior.math.Angle;
import ru.otus.templatepatterns.game.behavior.role.rotating.RotatingObject;

@RequiredArgsConstructor
public class Rotate implements Command {
    private final RotatingObject rotating;

    @Override
    public void execute() {
        rotating.setAngle(Angle.plus(rotating.getAngle(), rotating.getAngularVelocity()));
    }
}
