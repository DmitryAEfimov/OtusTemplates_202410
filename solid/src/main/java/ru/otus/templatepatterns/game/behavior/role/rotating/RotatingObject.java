package ru.otus.templatepatterns.game.behavior.role.rotating;

import ru.otus.templatepatterns.game.behavior.math.Angle;
import ru.otus.templatepatterns.game.behavior.math.AngularVelocity;

public interface RotatingObject {
    void setAngle(Angle angle);
    Angle getAngle();
    AngularVelocity getAngularVelocity();
}
