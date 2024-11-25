package ru.otus.templatepatterns.game.behavior.role.fuel;

import ru.otus.templatepatterns.game.behavior.attributes.InstantConsumption;

public interface CheckFuelObject {
    boolean isFuelEnough(InstantConsumption consumption);

    InstantConsumption getInstantConsumption();
}
