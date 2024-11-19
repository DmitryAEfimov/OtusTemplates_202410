package ru.otus.templatepatterns.common.exception;

import ru.otus.templatepatterns.common.command.Command;

/**
 * Контекст ошибки выполнения команды.
 *
 * <p>
 * Оба параметра могут быть пустыми. Пустое значение означает, что во время выполнения команды ошибка возникла, но
 * конкретное тип команды и/или ошибки в рамках данного контекста не интересен
 *
 * @param commandClass
 *         Runtime класс команды, исполнение которой привело к ошибке
 * @param exceptionClass
 *         Runtime класс ошибки, которая возникла, в ходе выполнения команды
 */
public record ExceptionContext(Class<? extends Command> commandClass, Class<? extends Exception> exceptionClass) {}
