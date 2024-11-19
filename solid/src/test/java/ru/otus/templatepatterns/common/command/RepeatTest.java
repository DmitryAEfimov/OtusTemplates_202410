package ru.otus.templatepatterns.common.command;

import org.mockito.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class RepeatTest {
    @Mock
    private Command mockCommand;

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
    public void execute_shouldExecuteEnclosedCommand() {
        new Repeat(mockCommand, new IOException("mockIOException")).execute();

        verify(mockCommand).execute();
    }
}
