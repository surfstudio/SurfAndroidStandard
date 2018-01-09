package ru.surfstudio.android.core.ui.base.delegate.manager;

/**
 * интерфейс провайдера {@link ScreenEventDelegateManager}
 * на каждый вызов {@link #get()} возвращет актуальный {@link ScreenEventDelegateManager} для экрана
 * провайдер необходим так как врея жизни ScreenEventDelegateManager  меньше времени жизни скоупа экрана
 */
public interface ScreenEventDelegateManagerProvider {

    ScreenEventDelegateManager get();
}
