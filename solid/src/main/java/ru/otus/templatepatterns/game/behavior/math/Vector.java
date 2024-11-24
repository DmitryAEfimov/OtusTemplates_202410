package ru.otus.templatepatterns.game.behavior.math;

/**
 * @param x Координата вектора по оси x
 * @param y Координата вектора по оси y
 */
public record Vector(double x, double y) {
    public static Vector plus(Vector a, Vector b) {
        return new Vector(a.x+b.x, a.y+b.y);
    }
}
