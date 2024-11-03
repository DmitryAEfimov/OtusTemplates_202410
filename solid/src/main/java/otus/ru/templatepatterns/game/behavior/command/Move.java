package otus.ru.templatepatterns.game.behavior.command;

import lombok.RequiredArgsConstructor;
import otus.ru.templatepatterns.game.behavior.math.Vector;
import otus.ru.templatepatterns.game.behavior.role.moving.MovingObject;

@RequiredArgsConstructor
public class Move {
    private final MovingObject moving;

    public void execute() {
        moving.setPosition(Vector.plus(moving.getPosition(), moving.getVelocity()));
    }
}
