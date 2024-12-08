package ru.otus.templatepatterns.common.command;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.LoggingEvent;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.otus.templatepatterns.game.behavior.command.Move;

import java.io.IOException;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

public class LogWriterTest {
    @Mock
    private Appender<LoggingEvent> mockAppender;
    @Mock
    private Move mockMove;

    private Logger rootLogger;
    private final CapturingAppender logCaptor = new CapturingAppender();
    private AutoCloseable autoCloseable;

    @BeforeClass
    public void init() {
        autoCloseable = openMocks(this);
        rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ((ch.qos.logback.classic.Logger) rootLogger).addAppender(logCaptor);
        logCaptor.start();
    }

    @AfterClass
    public void tearDown() throws Exception {
        logCaptor.stop();
        rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ((ch.qos.logback.classic.Logger) rootLogger).detachAppender(logCaptor);
        autoCloseable.close();
    }

    @Test
    public void execute_shouldLog() {
        new LogWriter(mockMove, new IOException("mockIOException")).execute();

        var loggingEvent = logCaptor.event;
        assertEquals(loggingEvent.getLevel(), Level.ERROR);
        assertEquals(loggingEvent.getFormattedMessage(),
                     "Exception type java.io.IOException: mockIOException, command: ru.otus.templatepatterns.game.behavior.command.Move");
    }

    private static class CapturingAppender extends AppenderBase<ILoggingEvent> {
        private ILoggingEvent event;

        @Override
        public void append(ILoggingEvent event) {
            this.event = event;
        }
    }
}
