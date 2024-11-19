package ru.otus.templatepatterns.game.behavior.role.moving;

import ru.otus.templatepatterns.game.behavior.math.Vector;

public interface MovingObject {
    void setPosition(Vector velocity);

    Vector getPosition();

    Vector getVelocity();
}
