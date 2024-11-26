package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.role.fuel.TankerObject;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class BurnFuelTest {
    @Mock
    private TankerObject mockTanker;
    @Mock
    private Exception mockException;

    private BurnFuel tested;
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
        reset(mockTanker, mockException);
        tested = new BurnFuel(mockTanker);
    }

    @Test
    public void execute_whenSuccess_shouldInvokeTankerObject() {
        doNothing().when(mockTanker).burnFuel();

        tested.execute();

        verify(mockTanker).burnFuel();
    }

    @Test
    public void execute_whenBurnFuelFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("burnFuelException");
        doAnswer(invocation -> {
            throw mockException;
        }).when(mockTanker).burnFuel();

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "burnFuelException");
    }
}
