package ru.surfstudio.android.core.mvp.delegate.factory;

/**
 * Контейнер для {@link MvpScreenDelegateFactory}
 * Позволяет заменить фабрику делегатов для всего приложения,
 * тем самым изменить логику всех MVP экранов
 */

public class MvpScreenDelegateFactoryContainer {

    private static MvpScreenDelegateFactory factory = new DefaultMvpScreenDelegateFactory();

    public static MvpScreenDelegateFactory get() {
        return factory;
    }

    public static void set(MvpScreenDelegateFactory factory) {
        MvpScreenDelegateFactoryContainer.factory = factory;
    }
}
