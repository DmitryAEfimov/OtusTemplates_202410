package otus.ru.templatepatterns.game.behavior.math;

/**
 * @param module      Модуль скорости вращения
 * @param sectorCount Количество секторов, для вычисления дискретного изменения угла поворота
 */
public record AngularVelocity(int module, short sectorCount) {}
