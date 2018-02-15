package ru.surfstudio.android.core.ui.base.screen.configurator;

/**
 * Интерфейс конфигуратора экрана, инкапсулирует всю логику работы с даггером
 * и предоставляет уникальное имя экрана для внутренней логики
 */
public interface Configurator {
    /**
     * конфигурирует экран
     */
    void run();
}
