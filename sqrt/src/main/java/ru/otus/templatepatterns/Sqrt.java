package ru.otus.templatepatterns;

import java.util.stream.Stream;

public class Sqrt {
    public double[] solve(double a, double b, double c, double epsilon) {
        validateParams(a, b, c, epsilon);

        var positiveEpsilon = Math.abs(epsilon);
        var discriminant = calcDiscriminant(a, b, c);

        if (Math.abs(a) < positiveEpsilon) {
            throwException("Коэффициент a не должен быть равен 0");
        }

        if (discriminant < -positiveEpsilon) {
            throwException("Решение невозможно");
        }

        if (Math.abs(discriminant) < positiveEpsilon) {
            return new double[] {-c / a, -c / a};
        }

        var x1 = (-b + Math.sqrt(discriminant)) / (2 * a * c);
        var x2 = (-b - Math.sqrt(discriminant)) / (2 * a * c);

        return new double[] {x1, x2};
    }

    private void validateParams(double a, double b, double c, double epsilon) {
        var result = Stream.of(a, b, c, epsilon).allMatch(this::isFinite);
        if (!result) {
            throwException("Все коэффициенты должны быть исчисляемыми");
        }
    }

    private void throwException(String message) {
        throw new IllegalArgumentException(message);
    }

    private boolean isFinite(double value) {
        return Double.isFinite(value);
    }

    private double calcDiscriminant(double a, double b, double c) {
        return Math.pow(b, 2) - 4 * a * c;
    }
}
