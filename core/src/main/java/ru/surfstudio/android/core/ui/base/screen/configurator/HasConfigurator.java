package ru.surfstudio.android.core.ui.base.screen.configurator;

/**
 * Интерфейс для экранов, имеющих конфигуратор
 */
public interface HasConfigurator {
    Configurator createConfigurator();

    Configurator getConfigurator();
}
