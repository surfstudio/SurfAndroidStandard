package ru.surfstudio.android.core.mvp.presenter;


import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider;

/**
 * зависимости базового презентера
 * Обернуты в отдельный обьект чтобы не занимать много места в конструкторах классов наследников {@link BasePresenter}
 */
public class BasePresenterDependency {

    private SchedulersProvider schedulersProvider;
    private ScreenState screenState;
    private ScreenEventDelegateManager eventDelegateManager;

    private ErrorHandler errorHandler;

    private ActivityNavigator activityNavigator;
    private ConnectionProvider connectionProvider;

    public BasePresenterDependency(SchedulersProvider schedulersProvider,
                                   ScreenState screenState,
                                   ScreenEventDelegateManager eventDelegateManager,
                                   ErrorHandler errorHandler, ConnectionProvider connectionProvider,
                                   ActivityNavigator activityNavigator) {
        this.schedulersProvider = schedulersProvider;
        this.screenState = screenState;
        this.eventDelegateManager = eventDelegateManager;
        this.errorHandler = errorHandler;
        this.connectionProvider = connectionProvider;
        this.activityNavigator = activityNavigator;
    }

    public SchedulersProvider getSchedulersProvider() {
        return schedulersProvider;
    }

    public ScreenEventDelegateManager getEventDelegateManager() {
        return eventDelegateManager;
    }

    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}
