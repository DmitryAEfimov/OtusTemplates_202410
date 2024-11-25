package ru.otus.templatepatterns.game.behavior.role.moving;

import ru.otus.templatepatterns.game.behavior.attributes.Angle;
import ru.otus.templatepatterns.game.behavior.attributes.Vector;

public interface ChangingVelocityObject {
    void setVelocity(Vector velocity);

    void setAngle(Angle movingAngle);
}
