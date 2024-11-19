package ru.otus.templatepatterns.game;

import ru.otus.templatepatterns.common.command.Command;
import ru.otus.templatepatterns.common.command.ExceptionHandlerResolver;
import ru.otus.templatepatterns.ioc.IoC;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
    private Boolean stop;

    public Game() {
        IoC.register("CommandQueue", new ConcurrentLinkedQueue<>());
    }

    public void start() {
        stop = false;
        @SuppressWarnings("unchecked") var queue = (Queue<Command>) IoC.resolve("CommandQueue");
        while (!stop) {
            processQueue(queue);
        }
    }

    public synchronized void terminate() {
        stop = true;
    }

    private void processQueue(Queue<Command> queue) {
        var command = queue.poll();
        if (command != null) {
            try {
                command.execute();
            } catch (Exception e) {
                var handlerRegistry = (ExceptionHandlerResolver) IoC.resolve("ExceptionHandlerRegistry");
                var exceptionHandler = handlerRegistry.resolve(command, e);
                exceptionHandler.execute();
            }
        }
    }
}
