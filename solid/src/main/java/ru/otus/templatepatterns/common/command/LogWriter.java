package ru.otus.templatepatterns.common.command;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class LogWriter implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogWriter.class);
    private static final String EXCEPTION_LOG_TEMPLATE = "Exception type {}: {}, command: {}";

    private final Command command;
    private final Exception exception;

    @Override
    public void execute() {
        LOGGER.error(EXCEPTION_LOG_TEMPLATE, exception.getClass().getName(), exception.getMessage(), command.getClass()
                                                                                                            .getName());
    }
}
