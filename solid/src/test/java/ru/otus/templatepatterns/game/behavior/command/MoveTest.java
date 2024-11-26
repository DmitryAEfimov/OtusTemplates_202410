package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.attributes.Vector;
import ru.otus.templatepatterns.game.behavior.role.moving.MovingObject;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MoveTest {
    @Mock
    private MovingObject mockMoving;
    @Mock
    private Exception mockException;

    private Move tested;
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
        reset(mockMoving, mockException);
        tested = new Move(mockMoving);
    }

    @Test(dataProvider = "vectorProvider")
    public void execute_whenSuccess_shouldAddVectorsByCoords(Vector position, Vector velocity, Vector expectedVector) {
        when(mockMoving.getPosition()).thenReturn(position);
        when(mockMoving.getVelocity()).thenReturn(velocity);
        var vectorCaptor = ArgumentCaptor.forClass(Vector.class);

        tested.execute();

        verify(mockMoving).setPosition(vectorCaptor.capture());
        var result = vectorCaptor.getValue();
        assertEquals(result, expectedVector);
    }

    @Test
    public void execute_whenGetPositionFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("getPositionException");
        when(mockMoving.getPosition()).thenAnswer(invocation -> {throw mockException;});

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "getPositionException");
    }

    @Test
    public void execute_whenGetVelocityFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("getVelocityException");
        when(mockMoving.getVelocity()).thenAnswer(invocation -> {throw mockException;});

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "getVelocityException");
    }

    @Test
    public void execute_whenSetPositionFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("setPositionException");
        when(mockMoving.getPosition()).thenReturn(new Vector(1, 1));
        when(mockMoving.getVelocity()).thenReturn(new Vector(1, 1));
        doAnswer(invocation -> {throw mockException;}).when(mockMoving).setPosition(any(Vector.class));

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "setPositionException");
    }

    @DataProvider(name = "vectorProvider")
    public Object[][] vectorProvider() {
        return new Object[][] {{new Vector(0, 0), new Vector(0, 0), new Vector(0, 0)},
                               {new Vector(0, 0), new Vector(1, 0), new Vector(1, 0)},
                               {new Vector(0, -1), new Vector(-1, 0), new Vector(-1, -1)},
                               {new Vector(12, 5), new Vector(-7, 3), new Vector(5, 8)}};
    }
}
