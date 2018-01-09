package ru.surfstudio.android.core.ui.base.delegate.manager;


import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.delegate.SupportScreenEventDelegation;

/**
 * провайдер {@link ScreenEventDelegateManager} для экрана основанного на Activity
 * на каждый вызов {@link #get()} возвращет актуальный {@link ScreenEventDelegateManager} для экрана
 * провайдер необходим так как врея жизни ScreenEventDelegateManager  меньше времени жизни скоупа экрана
 */
public class FragmentScreenEventDelegateManagerProvider implements ScreenEventDelegateManagerProvider {

    private FragmentProvider fragmentProvider;

    public FragmentScreenEventDelegateManagerProvider(FragmentProvider fragmentProvider) {
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    public ScreenEventDelegateManager get() {
        return ((SupportScreenEventDelegation) fragmentProvider.get()).getScreenEventDelegateManager();
    }
}
