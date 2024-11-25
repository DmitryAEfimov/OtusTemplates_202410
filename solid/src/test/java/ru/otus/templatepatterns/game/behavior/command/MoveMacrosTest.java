package ru.otus.templatepatterns.game.behavior.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MoveMacrosTest {
    @Mock
    private CheckFuel mockCheckFuel;
    @Mock
    private Move mockMove;
    @Mock
    private BurnFuel mockBurnFuel;
    @Mock
    private Exception mockException;

    private Macros tested;
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
        reset(mockCheckFuel, mockMove, mockBurnFuel, mockException);
        tested = new Macros(List.of(mockCheckFuel, mockMove, mockBurnFuel));
    }

    @Test
    public void execute_whenSuccess_shouldInvokeAllCommandsSequentially() {
        tested.execute();

        verify(mockCheckFuel).execute();
        verify(mockMove).execute();
        verify(mockBurnFuel).execute();
    }

    @Test
    public void execute_whenCommandFailed_shouldNotInvokeRestCommands() {
        when(mockException.getMessage()).thenReturn("internalCommandException");
        doAnswer(invocation -> {
            throw mockException;
        }).when(mockMove).execute();

        var throwable = catchThrowable(() -> tested.execute());

        assertNotNull(throwable);
        assertEquals(throwable.getMessage(), "internalCommandException");

        verify(mockCheckFuel).execute();
        verify(mockMove).execute();
        verifyNoInteractions(mockBurnFuel);
    }
}
