package ru.otus.templatepatterns.game.behavior.attributes;

/**
 * @param direction
 *         Угол, описывающий ориентацию игрового объекта, либо направление движения
 */
public record Angle(short direction) {
    public static Angle plus(Angle angle, AngularVelocity velocity) {
        var diff = calcAngle(angle, velocity);
        return new Angle(diff);
    }

    private static short calcAngle(Angle angle, AngularVelocity velocity) {
        var diff = angle.direction + velocity.module();
        diff = diff < 0 ? velocity.sectorCount() - Math.abs(diff) : diff;
        return (short) (diff % velocity.sectorCount());
    }
}
