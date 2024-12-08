package ru.otus.templatepatterns.common.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.command.Move;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertTrue;

public class ExceptionHandlerResolverTest {
    @Mock
    private Command mockCommand;
    @Mock
    private Move mockMove;
    @Mock
    private Exception mockException;

    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test(dataProvider = "illegalResourceProvider")
    public void init_whenResourceIsIllegal_shouldResolveToDefaultHandler(String resourceName) {
        var registry = new ExceptionHandlerResolver(resourceName);

        var result = registry.resolve(null, null);

        assertTrue(result instanceof ConsoleWriter);
    }

    @Test
    public void init_whenExceptionHandlerIsNotDefined_shouldResolveToDefaultHandler() {
        var registry = new ExceptionHandlerResolver("withoutExceptionHandler.yaml");

        var result = registry.resolve(mockCommand, new RuntimeException());
        assertTrue(result instanceof ConsoleWriter);
    }

    @Test
    public void init_whenExceptionContextCommandIsNotDefined_shouldMatchAnyCommand() {
        var registry = new ExceptionHandlerResolver("withoutCommandExceptionHandler.yaml");

        var result = registry.resolve(mockCommand, new RuntimeException());
        assertTrue(result instanceof LogWriter);
    }

    @Test
    public void init_whenExceptionContextExceptionIsNotDefined_shouldMatchAnyException() {
        var registry = new ExceptionHandlerResolver("withoutExceptionExceptionHandler.yaml");

        var result = registry.resolve(mockMove, mockException);
        assertTrue(result instanceof LogWriter);
    }

    @Test
    public void init_whenExceptionContextBothCommandAndExceptionIsNotDefined_shouldMatchAny() {
        var registry = new ExceptionHandlerResolver("withoutBothCommandAndExceptionExceptionHandler.yaml");

        var result = registry.resolve(mockCommand, mockException);
        assertTrue(result instanceof LogWriter);
    }

    @Test
    public void init_whenNoSuchClassFound_shouldResolveAsNull() {
        var registry = new ExceptionHandlerResolver("noSuchClassExceptionHandler.yaml");

        var result = registry.resolve(null, new RuntimeException());
        assertTrue(result instanceof LogWriter);

        result = registry.resolve(mockMove, null);
        assertTrue(result instanceof DelayedLogWriter);

        result = registry.resolve(null, null);
        assertTrue(result instanceof Repeat);

        result = registry.resolve(mockMove, new RuntimeException());
        assertTrue(result instanceof ConsoleWriter);
    }

    @DataProvider(name = "illegalResourceProvider")
    private Object[][] illegalResourceProvider() {
        return new Object[][] {
                {null},
                {"absentResource"},
                {"emptyExceptionHandler.yaml"},
                {"topLevelKeyOnlyExceptionHandler.yaml"}
        };
    }
}
