package ru.otus.templatepatterns.game;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.common.command.EmptyCommand;
import ru.otus.templatepatterns.common.command.ExceptionHandlerResolver;
import ru.otus.templatepatterns.ioc.IoC;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GameTest {
    @Mock
    private Command mockCommand;
    @Mock
    private BiFunction<Command, Exception, Command> mockExcHandler;
    @Mock
    private ExceptionHandlerResolver mockExcHandlerRegistry;

    private Game tested;
    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
        IoC.register("ExceptionHandlerRegistry", mockExcHandlerRegistry);
    }

    @AfterClass
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @BeforeMethod
    public void setup() throws Exception {
        reset(mockCommand, mockExcHandler, mockExcHandlerRegistry);
        tested = new Game();
        //Каждый вызов команды выполняется 50 мс
        doAnswer(invocation -> {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return null;
        }).when(mockCommand).execute();
        when(mockExcHandlerRegistry.resolve(any(Command.class), any(Exception.class))).thenAnswer(invocation -> {
            var command = (Command) invocation.getArgument(0);
            var exception = (Exception) invocation.getArgument(1);

            return mockExcHandler.apply(command, exception);
        });
    }

    @AfterMethod
    public void close() {
        tested.terminate();
    }

    @Test
    public void init_shouldInitQueue() {
        var queue = IoC.<Queue<Command>>resolve("CommandQueue");
        assertNotNull(queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void terminate_shouldStopPollQueue() throws InterruptedException {
        int initialQueueSize = 150;
        var queue = IoC.<Queue<Command>>resolve("CommandQueue");
        IntStream.range(0, initialQueueSize).forEach(i -> queue.add(mockCommand));

        startGame(queue);
        tested.terminate();

        assertTrue(!queue.isEmpty() && queue.size() <= initialQueueSize);
    }

    @Test(dataProvider = "queueFulfillmentProvider")
    public void poll_whenNoException_shouldInfinityLoop(int queueSize) throws Exception {
        var queue = IoC.<Queue<Command>>resolve("CommandQueue");
        IntStream.range(0, queueSize).forEach(i -> queue.add(mockCommand));

        startGame(queue);

        verify(mockCommand, times(queueSize)).execute();
        verifyNoInteractions(mockExcHandler);
    }

    @Test
    public void poll_whenExecutionException_shouldThrowAndContinue() throws Exception {
        int queueSize = 10;
        var queue = IoC.<Queue<Command>>resolve("CommandQueue");
        IntStream.range(0, queueSize).forEach(i -> queue.add(mockCommand));

        doAnswer(invocation -> {
            throw new IOException();
        }).when(mockCommand).execute();

        when(mockExcHandler.apply(eq(mockCommand), any(Exception.class))).thenReturn(new EmptyCommand());
        startGame(queue);

        verify(mockExcHandlerRegistry, times(queueSize)).resolve(any(Command.class), any(Exception.class));
        verify(mockCommand, times(queueSize)).execute();
        assertTrue(queue.isEmpty());
    }

    private void startGame(Queue<Command> queue) throws InterruptedException {
        var executorService = Executors.newFixedThreadPool(2);
        var latch = new CountDownLatch(1);

        executorService.submit(() -> tested.start());
        executorService.submit(() -> {
            try {
                while (!queue.isEmpty()) {
                    // за это время выполняется ~ 2 команд
                    Thread.sleep(100L);
                }
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // за это время выполняется ~ 2*10*5=100 команд
        latch.await(5, TimeUnit.SECONDS);
    }

    @DataProvider(name = "queueFulfillmentProvider")
    private Object[][] queueFulfillmentProvider() {
        return new Object[][] {{0}, {38}};
    }
}