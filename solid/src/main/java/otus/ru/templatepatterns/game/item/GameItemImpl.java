package otus.ru.templatepatterns.game.item;

import java.util.HashMap;
import java.util.Map;

public class GameItemImpl implements GameItem {
    private final Map<String, Object> properties;

    public GameItemImpl() {
        this.properties = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String propertyName) {
        return (T) properties.get(propertyName);
    }

    @Override
    public <T> void setProperty(String propertyName, T propertyValue) {
        properties.put(propertyName, propertyValue);
    }
}
