package ru.surfstudio.android.core.ui.base.delegate.manager;


import ru.surfstudio.android.core.ui.base.delegate.NewIntentDelegate;
import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.delegate.SupportScreenEventDelegation;

/**
 * базовый класс менеджера {@link ScreenEventDelegate} для фрагмента
 * занимается регистрацией оповещением делегатов о событиях экрана
 * Регистрация делегата {@link NewIntentDelegate} происходит в ScreenEventDelegateManager родительской активити
 */
public class FragmentScreenEventDelegateManager extends BaseScreenEventDelegateManager {

    private SupportScreenEventDelegation activitySupportEventDelegation;

    public FragmentScreenEventDelegateManager(SupportScreenEventDelegation activitySupportEventDelegation) {
        this.activitySupportEventDelegation = activitySupportEventDelegation;
    }

    @Override
    public void registerDelegate(ScreenEventDelegate delegate) {
        if(NewIntentDelegate.class.isInstance(delegate)){
            activitySupportEventDelegation.getScreenEventDelegateManager().registerDelegate(delegate);
        } else {
            super.registerDelegate(delegate);
        }
    }
}
