package ru.surfstudio.android.core.ui.base.screen.configurator;


import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

/**
 *
 *
 */
public interface ViewConfigurator<P extends PersistentScope> extends Configurator<P> {

    ScreenComponent getScreenComponent();
}
