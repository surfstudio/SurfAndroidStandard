package ru.surfstudio.android.mvp.widget.delegate.factory;

/**
 * Контейнер для {@link MvpWidgetDelegateFactory}
 * Позволяет заменить фабрику делегатов для всего приложения,
 * тем самым изменить логику всех виджетов
 */

public class MvpWidgetDelegateFactoryContainer {

    private static MvpWidgetDelegateFactory factory = new DefaultMvpWidgetDelegateFactory();

    public static MvpWidgetDelegateFactory get() {
        return factory;
    }

    public static void set(MvpWidgetDelegateFactory factory) {
        MvpWidgetDelegateFactoryContainer.factory = factory;
    }
}
