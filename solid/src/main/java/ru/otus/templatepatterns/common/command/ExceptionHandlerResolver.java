package ru.otus.templatepatterns.common.command;

import org.yaml.snakeyaml.Yaml;
import ru.otus.templatepatterns.common.exception.ExceptionContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class ExceptionHandlerResolver {
    private final ExceptionHandlerDictionary handlerDictionary;

    public ExceptionHandlerResolver(String dictionaryConfigResource) {
        this.handlerDictionary = new ExceptionHandlerDictionary();
        Optional.ofNullable(dictionaryConfigResource).ifPresentOrElse(this::load, this::loadDefault);
    }

    private void load(String resourceName) {
        var classLoader = getClass().getClassLoader();
        try (var resource = classLoader.getResourceAsStream(resourceName)) {
            Optional.ofNullable(resource)
                    .map(is -> (Map<String, List<Map<String, Object>>>) new Yaml().loadAs(resource, Map.class))
                    .map(raw -> raw.get("exceptionHandlerDictionary"))
                    .ifPresentOrElse(this::initDictionary, this::loadDefault);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private void loadDefault() {
        handlerDictionary.add(new ExceptionContext(null, null), (cmd, exc) -> new ConsoleWriter(cmd, exc));
    }

    private void initDictionary(List<Map<String, Object>> data) {
        loadDefault();
        data.forEach(this::addRowToDictionary);
    }

    private void addRowToDictionary(Map<String, Object> row) {
        var rawContext = (Map<String, String>) row.get("exceptionContext");
        var nullSafeContext = rawContext != null ? parseContext(rawContext) : new ExceptionContext(null, null);

        var rawHandler = (String) row.get("exceptionHandler");
        BiFunction<Command, Exception, Command> nullSafeHandler = (cmd, exc) -> rawHandler != null ? parseHandler(
                parseForClass(rawHandler, Command.class),
                cmd,
                exc) : new ConsoleWriter(cmd, exc);

        handlerDictionary.add(nullSafeContext, nullSafeHandler);
    }

    private ExceptionContext parseContext(Map<String, String> rawContext) {
        var commandClass = parseForClass(rawContext.get("command"), Command.class);
        var exceptionClass = parseForClass(rawContext.get("exception"), Exception.class);

        return new ExceptionContext(commandClass, exceptionClass);
    }

    private Command parseHandler(Class<? extends Command> commandType, Command cmd, Exception exc) {
        try {
            return commandType != null ? commandType.getDeclaredConstructor(Command.class, Exception.class)
                                                    .newInstance(cmd, exc) : new ConsoleWriter(cmd, exc);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException |
                 InstantiationException e) {
            return new ConsoleWriter(cmd, exc);
        }
    }

    private <T> Class<? extends T> parseForClass(String className, Class<T> objClass) {
        Class<? extends T> realClass;
        try {
            realClass = className != null ? (Class<? extends T>) Class.forName(className) : null;
        } catch (ClassNotFoundException e) {
            realClass = null;
        }

        return realClass;
    }

    public Command resolve(Command command, Exception exc) {
        return handlerDictionary.apply(command, exc);
    }
}
