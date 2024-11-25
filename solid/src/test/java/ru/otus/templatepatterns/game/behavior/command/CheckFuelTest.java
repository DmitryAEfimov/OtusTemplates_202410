package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.common.exception.NoFuelException;
import ru.otus.templatepatterns.game.behavior.attributes.InstantConsumption;
import ru.otus.templatepatterns.game.behavior.role.fuel.CheckFuelObject;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class CheckFuelTest {
    @Mock
    private CheckFuelObject mockCheckFuel;
    @Mock
    private Exception mockException;

    private CheckFuel tested;
    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @BeforeMethod
    public void setup() {
        reset(mockCheckFuel);
        when(mockCheckFuel.getInstantConsumption()).thenReturn(new InstantConsumption(1L));
        tested = new CheckFuel(mockCheckFuel);
    }

    @Test
    public void execute_whenEnoughFuel_shouldNotThrowException() {
        when(mockCheckFuel.isFuelEnough(any(InstantConsumption.class))).thenReturn(true);

        var throwable = catchThrowable(() -> tested.execute());
        assertNull(throwable);
    }

    @Test
    public void execute_whenNotEnoughFuel_shouldThrowException() {
        when(mockCheckFuel.isFuelEnough(any(InstantConsumption.class))).thenReturn(false);

        var throwable = catchThrowable(() -> tested.execute());
        assertTrue(throwable instanceof NoFuelException);
    }

    @Test
    public void execute_whenCheckFuelFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("checkFuelException");
        doAnswer(invocation -> {
            throw mockException;
        }).when(mockCheckFuel).isFuelEnough(any(InstantConsumption.class));

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "checkFuelException");
    }
}
