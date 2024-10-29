package otus.ru.templatepatterns.game.behavior.role.rotating;

import otus.ru.templatepatterns.game.behavior.math.Angle;
import otus.ru.templatepatterns.game.behavior.math.AngularVelocity;

public interface RotatingObject {
    void setAngle(Angle angle);
    Angle getAngle();
    AngularVelocity getAngularVelocity();
}
