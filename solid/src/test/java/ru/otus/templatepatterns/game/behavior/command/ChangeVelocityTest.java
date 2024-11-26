package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.attributes.Angle;
import ru.otus.templatepatterns.game.behavior.attributes.Vector;
import ru.otus.templatepatterns.game.behavior.role.moving.ChangingVelocityObject;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ChangeVelocityTest {
    @Mock
    private ChangingVelocityObject mockChangingVelocity;
    @Mock
    private Exception mockException;

    private Angle angle;
    private Vector vector;
    private ChangeVelocity tested;
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
        reset(mockChangingVelocity, mockException);

        vector = new Vector(1, 1);
        angle = new Angle((short) 1);
        tested = new ChangeVelocity(mockChangingVelocity, vector, angle);
    }

    @Test
    public void execute_whenSuccess_shouldInvokeChangingVelocityObject() {
        tested.execute();

        verify(mockChangingVelocity).setVelocity(vector);
        verify(mockChangingVelocity).setAngle(angle);
    }

    @Test
    public void execute_whenChangingVelocityFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("changingVelocityException");
        doAnswer(invocation -> {
            throw mockException;
        }).when(mockChangingVelocity).setVelocity(any(Vector.class));

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "changingVelocityException");
    }
}
