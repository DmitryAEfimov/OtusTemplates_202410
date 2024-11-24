package ru.otus.templatepatterns.common.command;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.Game;
import ru.otus.templatepatterns.ioc.IoC;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

public class ComplexExceptionHandlerStrategyTest {
    @Mock
    private ExceptionHandlerResolver mockExcHandlerRegistry;
    @Mock
    private EmptyCommand mockEmptyCommand;
    @Mock
    private RuntimeException mockRuntimeException;
    @Spy
    @InjectMocks
    private DelayedRepeat spyDelayedRepeat;
    @Spy
    @InjectMocks
    private RepeatLastTime spyRepeatLastTime;
    @Spy
    @InjectMocks
    private DelayedLogWriter spyDelayedLogWriter;
    @Spy
    private ConcurrentLinkedQueue<Command> spyCommandQueue;

    private ExecutorService executorService;
    private Game game;
    private AutoCloseable autoCloseable;


    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
        IoC.register("ExceptionHandlerRegistry", mockExcHandlerRegistry);
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @BeforeMethod
    private void setup() {
        reset(mockEmptyCommand, spyDelayedRepeat, spyRepeatLastTime, spyDelayedLogWriter, spyCommandQueue);
        doAnswer(invocation -> {
            throw mockRuntimeException;
        }).when(mockEmptyCommand).execute();

        game = new Game();
        spyCommandQueue.clear();
        IoC.register("CommandQueue", spyCommandQueue);
        IoC.<Queue<Command>>resolve("CommandQueue").add(mockEmptyCommand);
    }

    @AfterMethod
    private void stop() {
        game.terminate();
    }

    @Test
    public void strategy_repeatThenLog() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(Command.class);
        var exceptionCaptor = ArgumentCaptor.forClass(Exception.class);

        executorService.submit(() -> {
            when(mockExcHandlerRegistry.resolve(commandCaptor.capture(), exceptionCaptor.capture())).thenReturn(spyDelayedRepeat, spyDelayedLogWriter);
            game.start();
        });

        //Этого должно быть достаточно, что бы прогнать несколько пустых команд
        Thread.sleep(500L);

        verify(mockEmptyCommand, times(2)).execute();
        verify(spyDelayedRepeat).execute();
        verify(spyDelayedLogWriter).execute();

        // emptyCommand -> repeatingCommand -> writeToLogCommand
        var executedCommands = commandCaptor.getAllValues();
        assertEquals(executedCommands.size(), 2);
        verify(spyCommandQueue, times(executedCommands.size())).add(mockEmptyCommand);
        var thrownExceptions = exceptionCaptor.getAllValues();
        assertEquals(thrownExceptions.size(), 2);
        thrownExceptions.forEach(exc -> assertEquals(exc, mockRuntimeException));
    }

    @Test
    public void strategy_repeat2TimesThenLog() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(Command.class);
        var exceptionCaptor = ArgumentCaptor.forClass(Exception.class);

        executorService.submit(() -> {
            when(mockExcHandlerRegistry.resolve(commandCaptor.capture(), exceptionCaptor.capture())).thenReturn(spyDelayedRepeat, spyRepeatLastTime, spyDelayedLogWriter);
            game.start();
        });

        //Этого должно быть достаточно, что бы прогнать несколько пустых команд
        Thread.sleep(500L);

        verify(mockEmptyCommand, times(3)).execute();
        verify(spyDelayedRepeat).execute();
        verify(spyRepeatLastTime).execute();
        verify(spyDelayedLogWriter).execute();

        // emptyCommand -> repeatingCommand -> repeatingLastTime -> writeToLogCommand
        var executedCommands = commandCaptor.getAllValues();
        assertEquals(executedCommands.size(), 3);
        verify(spyCommandQueue, times(executedCommands.size())).add(mockEmptyCommand);
        var thrownExceptions = exceptionCaptor.getAllValues();
        assertEquals(thrownExceptions.size(), 3);
        thrownExceptions.forEach(exc -> assertEquals(exc, mockRuntimeException));
    }
}
