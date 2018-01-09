package ru.surfstudio.android.core.ui.base.delegate.manager;


import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.delegate.SupportScreenEventDelegation;

/**
 * провайдер {@link ScreenEventDelegateManager} для экрана основанного на Activity
 * на каждый вызов {@link #get()} возвращет актуальный {@link ScreenEventDelegateManager} для экрана
 * провайдер необходим так как врея жизни ScreenEventDelegateManager  меньше времени жизни скоупа экрана
 */
public class ActivityScreenEventDelegateManagerProvider implements ScreenEventDelegateManagerProvider {

    private ActivityProvider activityProvider;

    public ActivityScreenEventDelegateManagerProvider(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    @Override
    public ScreenEventDelegateManager get(){
        return ((SupportScreenEventDelegation)activityProvider.get()).getScreenEventDelegateManager();
    }
}
