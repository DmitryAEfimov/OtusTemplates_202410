package ru.otus.templatepatterns.game.item;

public interface GameItem {
    <T> T getProperty(String propertyName);

    <T> void setProperty(String propertyName, T propertyValue);
}
