package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.attributes.Angle;
import ru.otus.templatepatterns.game.behavior.attributes.AngularVelocity;
import ru.otus.templatepatterns.game.behavior.role.rotating.RotatingObject;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RotateTest {
    private static final short SECTOR_CNT = 20;

    @Mock
    private RotatingObject mockRotating;
    @Mock
    private Exception mockException;

    private Rotate tested;
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
        reset(mockRotating, mockException);
        tested = new Rotate(mockRotating);
    }

    @Test(dataProvider = "angleChangeProvider")
    public void execute_whenSuccess_shouldRotateBySectors(Angle direction, AngularVelocity angularVelocity, Angle expectedDirection) {
        when(mockRotating.getAngle()).thenReturn(direction);
        when(mockRotating.getAngularVelocity()).thenReturn(angularVelocity);
        var angleCaptor = ArgumentCaptor.forClass(Angle.class);

        tested.execute();

        verify(mockRotating).setAngle(angleCaptor.capture());
        var result = angleCaptor.getValue();
        assertEquals(result, expectedDirection);
    }

    @Test
    public void execute_whenGetPositionFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("getAngleException");
        when(mockRotating.getAngle()).thenAnswer(invocation -> {throw mockException;});

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "getAngleException");
    }

    @Test
    public void execute_whenGetAngleFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("getAngularVelocityException");
        when(mockRotating.getAngularVelocity()).thenAnswer(invocation -> {throw mockException;});

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "getAngularVelocityException");
    }

    @Test
    public void execute_whenSetPositionFail_shouldThrowException() {
        when(mockException.getMessage()).thenReturn("setAngleException");
        when(mockRotating.getAngle()).thenReturn(new Angle((short) 1));
        when(mockRotating.getAngularVelocity()).thenReturn(new AngularVelocity(1, SECTOR_CNT));
        doAnswer(invocation -> {throw mockException;}).when(mockRotating).setAngle(any(Angle.class));

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "setAngleException");
    }

    @DataProvider(name = "angleChangeProvider")
    public Object[][] angleChangeProvider() {
        return new Object[][] {{new Angle((short) 0), new AngularVelocity(1, SECTOR_CNT), new Angle((short) 1)},
                               {new Angle((short) 0),
                                new AngularVelocity(SECTOR_CNT, SECTOR_CNT),
                                new Angle((short) 0)},
                               {new Angle((short) 5), new AngularVelocity(-13, SECTOR_CNT), new Angle((short) 12)},
                               {new Angle((short) 5), new AngularVelocity(18, SECTOR_CNT), new Angle((short) 3)}};
    }
}
