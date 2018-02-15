package ru.surfstudio.android.core.ui.base.screen.delegate.factory;

/**
 * Контейнер для {@link ScreenDelegateFactory}
 * Позволяет заменить фабрику делегатов для всего приложения,
 * тем самым изменить логику всех экранов
 */

public class ScreenDelegateFactoryContainer {

    private static ScreenDelegateFactory factory = new DefaultScreenDelegateFactory();

    public static ScreenDelegateFactory get() {
        return factory;
    }

    public static void set(ScreenDelegateFactory factory) {
        ScreenDelegateFactoryContainer.factory = factory;
    }
}
