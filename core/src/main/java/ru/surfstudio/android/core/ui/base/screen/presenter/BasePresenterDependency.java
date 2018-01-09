package ru.surfstudio.android.core.ui.base.screen.presenter;


import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.delegate.manager.ScreenEventDelegateManagerProvider;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.newintent.NewIntentManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;

/**
 * зависимости базового презентера
 * Обернуты в отдельный обьект чтобы не занимать много места в конструкторах классов наследников {@link BasePresenter}
 */
@PerScreen
public class BasePresenterDependency {

    private SchedulersProvider schedulersProvider;
    private ScreenEventDelegateManagerProvider delegateManagerProvider;

    private ActivityNavigator activityNavigator;
    private PermissionManager permissionManager;
    private NewIntentManager newIntentManager;

    @Inject
    public BasePresenterDependency(SchedulersProvider schedulersProvider,
                                   ScreenEventDelegateManagerProvider delegateManagerProvider,
                                   ActivityNavigator activityNavigator,
                                   PermissionManager permissionManager,
                                   NewIntentManager newIntentManager) {
        this.schedulersProvider = schedulersProvider;
        this.delegateManagerProvider = delegateManagerProvider;
        this.activityNavigator = activityNavigator;
        this.permissionManager = permissionManager;
        this.newIntentManager = newIntentManager;
    }

    public SchedulersProvider getSchedulersProvider() {
        return schedulersProvider;
    }

    public ScreenEventDelegateManagerProvider getDelegateManagerProvider() {
        return delegateManagerProvider;
    }

    public ScreenEventDelegate[] getScreenEventDelegates() {
        return new ScreenEventDelegate[]{ activityNavigator, permissionManager, newIntentManager };
    }

    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }
}
