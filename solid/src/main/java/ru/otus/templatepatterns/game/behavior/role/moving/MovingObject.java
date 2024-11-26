package ru.otus.templatepatterns.game.behavior.role.moving;

import ru.otus.templatepatterns.game.behavior.attributes.Vector;

public interface MovingObject {
    void setPosition(Vector velocity);

    Vector getPosition();

    Vector getVelocity();
}
