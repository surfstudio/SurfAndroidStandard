package ru.surfstudio.android.core.ui.base.screen.delegate.factory;

/**
 * Created by makstuev on 30.01.2018.
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
