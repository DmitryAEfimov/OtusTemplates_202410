package ru.otus.templatepatterns;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class SqrtTest {
    private static final double EPSILON = 1.E-6;
    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test(dataProvider = "nearZeroProvider")
    public void testSqrt_whenAIsNearZero_shouldThrowException(double coefficient) {
        var sqrt = new Sqrt();
        assertThrows("Коэффициент a не должен быть равен 0", IllegalArgumentException.class, () -> sqrt.solve(coefficient, 1, 1, EPSILON));
    }

    @Test(dataProvider = "nearZeroProvider")
    public void testSqrt_whenDiscriminantIsLessThenZero_shouldThrowException(double coefficient) {
        var sqrt = new Sqrt();
        assertThrows("Решение невозможно", IllegalArgumentException.class, () -> sqrt.solve(1.-coefficient, coefficient, 1.+coefficient, EPSILON));
    }

    @Test
    public void testSqrt_whenEpsilonIsLessThenZero_shouldCompareWithAbsEpsilon() {
        var sqrt = new Sqrt();
        assertThrows("Решение невозможно", IllegalArgumentException.class, () -> sqrt.solve(1., 0, 1., -EPSILON));
    }

    @Test(dataProvider = "nearZeroProvider")
    public void testSqrt_whenDiscriminantIsNearZero_shouldReturnSingleRoot(double coefficient) {
        var sqrt = new Sqrt();
        var result = sqrt.solve(1.-coefficient, 2+coefficient, 1.+coefficient, EPSILON);

        compareResult(new double[] {-1.d, -1.d}, result);
    }

    @Test(dataProvider = "nearZeroProvider")
    public void testSqrt_whenDiscriminantIsGreaterThenZero_shouldReturnTwoRoots(double coefficient) {
        var sqrt = new Sqrt();
        var result = sqrt.solve(1.-coefficient, coefficient, -1.+coefficient, EPSILON);

        compareResult(new double[] {-1.d, 1.d}, result);
    }

    @Test(dataProvider = "notAFiniteProvider")
    public void testSqrt_whenNotAFinite_shouldThrowException(double a, double b, double c, double epsilon) {
        var sqrt = new Sqrt();
        assertThrows("Все коэффициенты должны быть исчисляемыми", IllegalArgumentException.class, () -> sqrt.solve(a, b, c, epsilon));
    }

    private void compareResult(double[] expected, double[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertTrue(Math.abs(expected[i]-actual[i]) <= EPSILON);
        }
    }

    @DataProvider(name = "notAFiniteProvider")
    private Object[][] notAFiniteProvider() {
        return new Object[][] {{Double.NaN, 1., 0, EPSILON},
                               {1., Double.NEGATIVE_INFINITY, 0, EPSILON},
                               {1., 1., Double.POSITIVE_INFINITY, EPSILON},
                               {1., 1., 1., Double.NaN}};
    }

    @DataProvider(name = "nearZeroProvider")
    private Object[][] nearZeroProvider() {
        return new Object[][] {{0}, {0.1*EPSILON}, {-0.1*EPSILON}};
    }
}