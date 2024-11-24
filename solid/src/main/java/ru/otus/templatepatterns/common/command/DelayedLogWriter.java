package ru.otus.templatepatterns.common.command;

import lombok.RequiredArgsConstructor;
import ru.otus.templatepatterns.ioc.IoC;

import java.util.Queue;

@RequiredArgsConstructor
public class DelayedLogWriter implements Command {
    private final Command command;
    private final Exception exception;

    @Override
    public void execute() {
        IoC.<Queue<Command>>resolve("CommandQueue").add(new LogWriter(command, exception));
    }
}
