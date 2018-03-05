package ru.surfstudio.android.core.ui.scope;

import ru.surfstudio.android.core.ui.configurator.Configurator;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.ScreenState;

/**
 * PersistentScope для экрана
 */
public abstract class ScreenPersistentScope extends PersistentScope {
    private final ScreenEventDelegateManager screenEventDelegateManager;
    private final ScreenState screenState;
    private final Configurator configurator;

    protected ScreenPersistentScope(ScreenEventDelegateManager screenEventDelegateManager,
                                    ScreenState screenState,
                                    Configurator configurator,
                                    String scopeId) {
        super(scopeId);
        this.screenEventDelegateManager = screenEventDelegateManager;
        this.screenState = screenState;
        this.configurator = configurator;
    }

    public ScreenEventDelegateManager getScreenEventDelegateManager() {
        return screenEventDelegateManager;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public Configurator getConfigurator() {
        return configurator;
    }
}
