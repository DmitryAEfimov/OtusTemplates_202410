package ru.otus.templatepatterns.common.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.common.exception.ExceptionContext;
import ru.otus.templatepatterns.game.behavior.command.Move;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertTrue;

public class ExceptionHandlerDictionaryTest {
    @Mock
    private BiFunction<Command, Exception, Command> mockExcHandler;
    @Mock
    private Move mockMove;

    private final ExceptionContext mockExcContext = new ExceptionContext(Move.class, IOException.class);
    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void add_whenBaseExceptionRegisteredInDictionary_shouldFindHandlerForAnyExceptionSubclasses() {
        when(mockExcHandler.apply(any(Command.class), any(Exception.class))).thenReturn(new EmptyCommand());

        var dictionary = new ExceptionHandlerDictionary();
        dictionary.add(mockExcContext, mockExcHandler);

        var result = dictionary.apply(mockMove, new InterruptedIOException());

        assertTrue(result instanceof EmptyCommand);
    }

    @Test
    public void find_whenEmptyContextIsAssigning_shouldReturnAssignedHandler() {
        when(mockExcHandler.apply(isNull(), isNull())).thenReturn(mock(LogWriter.class));

        var dictionary = new ExceptionHandlerDictionary();

        dictionary.add(null, mockExcHandler);
        var result = dictionary.apply(null, null);

        assertTrue(result instanceof LogWriter);
    }

    @Test
    public void find_whenExceptionContextIsNotFounded_shouldReturnDefaultContext() {
        var dictionary = new ExceptionHandlerDictionary();

        var result = dictionary.apply(mockMove, new IOException());

        assertTrue(result instanceof ConsoleWriter);
    }
}
