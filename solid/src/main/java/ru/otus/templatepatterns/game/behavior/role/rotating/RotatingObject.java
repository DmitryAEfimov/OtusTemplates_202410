package ru.otus.templatepatterns.game.behavior.role.rotating;

import ru.otus.templatepatterns.game.behavior.attributes.Angle;
import ru.otus.templatepatterns.game.behavior.attributes.AngularVelocity;

public interface RotatingObject {
    void setAngle(Angle orientationAngle);

    Angle getAngle();

    AngularVelocity getAngularVelocity();
}
