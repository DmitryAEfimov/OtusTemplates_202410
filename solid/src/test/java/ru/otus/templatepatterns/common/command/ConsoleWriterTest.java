package ru.otus.templatepatterns.common.command;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class ConsoleWriterTest {
    @Test
    public void execute_shouldPrintToConsole() throws Exception {
        var output = SystemLambda.tapSystemOut(() -> new ConsoleWriter(new EmptyCommand(),
                                                                       new IOException()).execute());

        assertEquals(output.trim(),
                     "No exception handler registered for [exception: java.io.IOException, command: ru.otus.templatepatterns.common.command.EmptyCommand]");
    }
}
