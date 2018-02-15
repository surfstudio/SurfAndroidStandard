package ru.surfstudio.android.core.ui.base.screen.configurator;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

/**
 * Интерфейс конфигуратора экрана, инкапсулирует всю логику работы с даггером
 * и предоставляет уникальное имя экрана для внутренней логики
 */
public interface Configurator<P extends PersistentScope> extends HasName {
    /**
     * конфигурирует экран
     */
    void run();

    /**
     * Устанавливает persistentScope, persistentScope не устанавливается в конструктор,
     * поскольку инстанс конфигуратора нужен для инициализации persistentScope, а именно нужно
     * уникальное имя экрана
     */
    void setPersistentScope(P persistentScope);
}
