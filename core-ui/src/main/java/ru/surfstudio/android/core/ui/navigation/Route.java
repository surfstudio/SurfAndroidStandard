package ru.surfstudio.android.core.ui.navigation;

/**
 * базовый интерфейс маршрута для навигатора
 * <p>
 * маршрут ответственен за:
 * определение, какой конкретно экран (диалог) открыть
 * <p>
 * маршрут может быть ответственен за:
 * - упаковку данных в Intent (Bundle) для передачи аргументов в экран, диалог
 * - распаковку данных из Intent (Bundle) переданных в экран, диалог
 * - упаковку данных в Intent для передачи результата на предыдущий экран
 * - распаковку данных результата выполнения экрана из Intent
 */
public interface Route {

    String EXTRA_FIRST = "EXTRA_FIRST";
    String EXTRA_SECOND = "EXTRA_SECOND";
    String EXTRA_THIRD = "EXTRA_THIRD";
    String EXTRA_FOURTH = "EXTRA_FOURTH";
    String EXTRA_FIFTH = "EXTRA_FIFTH";
    String EXTRA_SIXTH = "EXTRA_SIXTH";
}
