package ru.surfstudio.android.core.ui.base.delegate.manager;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.delegate.NewIntentDelegate;
import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;

/**
 * класс менеджера {@link ScreenEventDelegate} для Activty
 * занимается регистрацией оповещением делегатов о событиях экрана
 */
public class ActivityScreenEventDelegateManager  extends BaseScreenEventDelegateManager {

    public void onNewIntent(Intent intent) {
        Class<NewIntentDelegate> delegateType = NewIntentDelegate.class;
        for(NewIntentDelegate delegate : getDelegates(delegateType)){
            if(delegate.onNewIntent(intent)){
                return;
            }
        }
        logUnhandled(delegateType, "new Intent=" + intent);
    }
}
