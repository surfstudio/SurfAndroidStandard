package ru.surfstudio.android.core.ui.base.screen.configurator;

/**
 * Created by makstuev on 25.01.2018.
 */

public interface HasConfigurator {
    Configurator createConfigurator();

    Configurator getConfigurator();
}
