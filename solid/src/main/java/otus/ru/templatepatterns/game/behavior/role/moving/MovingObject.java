package otus.ru.templatepatterns.game.behavior.role.moving;

import otus.ru.templatepatterns.game.behavior.math.Vector;

public interface MovingObject {
    void setPosition(Vector velocity);

    Vector getPosition();

    Vector getVelocity();
}
