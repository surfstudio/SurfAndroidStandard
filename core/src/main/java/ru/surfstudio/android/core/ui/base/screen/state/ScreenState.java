package ru.surfstudio.android.core.ui.base.screen.state;

/**
 * Интерфейс для всех ScreenState
 * предоставляет текущее состояние экрана
 */

public interface ScreenState {
    /**
     * @return пересоздана ли иерархия вью после смены конфигурации
     * если экран быль только восстановлен с диска, то метод вернет false
     */
    boolean isViewRecreated();

    /**
     * @return пересоздана ли обьект экрана после смены конфигурации
     * если экран быль только восстановлен с диска, то метод вернет false
     */
    boolean isScreenRecreated();

    /**
     * @return уничтожен ли экран полностью и не будет восстановлен
     */
    boolean isCompletelyDestroyed();

    /**
     * @return восстановлен ли экран с диска
     * после смены конфигурации метод вернет true если раньше он был восстановлен
     * с диска
     */
    boolean isRestoredFromDisk();
}
