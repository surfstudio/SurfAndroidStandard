package ru.surfstudio.android.core.ui.base.screen.configurator;

import ru.surfstudio.android.core.ui.HasName;

/**
 * Интерфейс конфигуратора экрана, инкапсулирует всю логику работы с даггером
 */
public interface Configurator extends HasName {
    void run();
}
