package ru.otus.templatepatterns.game.item;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class GameItemImplTest {
    @Test(dataProvider = "objectProvider")
    public void addProperty_shouldSaveProperty(Object value, Class<?> type) {
        var item = new GameItemImpl();
        item.setProperty("property", value);

        var result = item.getProperty("property");
        assertNotNull(result);
        assertTrue(type.isInstance(result));
        assertEquals(value, result);
    }

    @Test
    public void getProperty_whenNoProperty_shouldReturnNull() {
        var item = new GameItemImpl();

        assertNull(item.getProperty("absentProperty"));
    }

    @DataProvider
    private Object[][] objectProvider() {
        return new Object[][] {{"value", String.class},
                               {10, Integer.class},
                               {new BigDecimal("145.344556547").setScale(2, RoundingMode.HALF_EVEN), BigDecimal.class}};
    }
}
