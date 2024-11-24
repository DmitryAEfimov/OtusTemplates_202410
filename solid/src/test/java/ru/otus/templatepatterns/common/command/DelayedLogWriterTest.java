package ru.otus.templatepatterns.common.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.ioc.IoC;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DelayedLogWriterTest {
    @Mock
    private Command mockCommand;

    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
        IoC.register("CommandQueue", new ConcurrentLinkedQueue<>());
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
        IoC.register("CommandQueue", new ConcurrentLinkedQueue<>());
    }

    @Test
    public void execute_shouldAddLoggingCommandToQueue() {
        new DelayedLogWriter(mockCommand, new IOException()).execute();

        var queue = IoC.<Queue<Command>>resolve("CommandQueue");

        assertEquals(queue.size(), 1);
        assertTrue(queue.peek() instanceof LogWriter);
        verifyNoInteractions(mockCommand);
    }
}
