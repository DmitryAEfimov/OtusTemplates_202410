package otus.ru.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import otus.ru.templatepatterns.game.behavior.math.Angle;
import otus.ru.templatepatterns.game.behavior.role.rotating.RotatingObject;

@RequiredArgsConstructor
public class Rotate {
    private final RotatingObject rotating;

    public void execute() {
        rotating.setAngle(Angle.plus(rotating.getAngle(), rotating.getAngularVelocity()));
    }
}
