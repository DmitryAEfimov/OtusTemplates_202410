package ru.otus.templatepatterns.common.command;

import ru.otus.templatepatterns.common.exception.ExceptionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class ExceptionHandlerDictionary implements BiFunction<Command, Exception, Command> {
    private static final ExceptionContext DEFAULT_EXCEPTION_CONTEXT = new ExceptionContext(null, null);
    private static final Class<Exception> BASE_EXCEPTION_CLASS = Exception.class;

    private final Map<ExceptionContext, BiFunction<Command, Exception, Command>> exceptionRegistry;

    public ExceptionHandlerDictionary() {
        exceptionRegistry = new HashMap<>();
        exceptionRegistry.put(DEFAULT_EXCEPTION_CONTEXT, (cmd, exc) -> new ConsoleWriter(cmd, exc));
    }

    public void add(ExceptionContext context, BiFunction<Command, Exception, Command> producer) {
        registerHandlerWithPropagation(context, producer);
    }

    @Override
    public Command apply(Command cmd, Exception exc) {
        var commandClass = Optional.ofNullable(cmd).map(Command::getClass).orElse(null);
        BiFunction<Command, Exception, Command>[] handler = new BiFunction[1];
        Optional.ofNullable(exc).map(Exception::getClass).ifPresent(exceptionClass -> {
            Class<?> currentExceptionClass = exceptionClass;
            while (BASE_EXCEPTION_CLASS.isAssignableFrom(currentExceptionClass)) {
                var matchedContext = new ExceptionContext(commandClass, (Class<? extends Exception>) currentExceptionClass);
                var defaultExceptionContext = new ExceptionContext(null, (Class<? extends Exception>) currentExceptionClass);
                var value = exceptionRegistry.getOrDefault(matchedContext, exceptionRegistry.get(defaultExceptionContext));
                if (value != null) {
                    handler[0] = value;
                    break;
                }
                currentExceptionClass = currentExceptionClass.getSuperclass();
            }
        });
        var producer = handler[0] != null ? handler[0]
                                          : exceptionRegistry.getOrDefault(new ExceptionContext(commandClass, null), exceptionRegistry.get(DEFAULT_EXCEPTION_CONTEXT));

        return producer.apply(cmd, exc);
    }

    private void registerHandlerWithPropagation(ExceptionContext context, BiFunction<Command, Exception, Command> producer) {
        var commandClass = Optional.ofNullable(context).map(ExceptionContext::commandClass).orElse(null);
        Optional.ofNullable(context).map(ExceptionContext::exceptionClass).ifPresentOrElse(exceptionClass -> {
            Class<?> currentExceptionClass = exceptionClass;
            while (BASE_EXCEPTION_CLASS.isAssignableFrom(currentExceptionClass)) {
                var previous = exceptionRegistry.putIfAbsent(new ExceptionContext(commandClass, (Class<? extends Exception>) currentExceptionClass), producer);
                if (previous != null) {
                    break;
                }
                currentExceptionClass = currentExceptionClass.getSuperclass();
            }
        }, () -> exceptionRegistry.put(new ExceptionContext(commandClass, null), producer));
    }
}
