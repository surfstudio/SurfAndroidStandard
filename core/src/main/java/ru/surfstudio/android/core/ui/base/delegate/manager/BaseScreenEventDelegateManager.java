package ru.surfstudio.android.core.ui.base.delegate.manager;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.surfstudio.android.core.ui.base.delegate.RequestPermissionsResultDelegate;
import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.delegate.activity.result.ActivityResultDelegate;

/**
 * базовый класс менеджера {@link ScreenEventDelegate}
 * занимается регистрацией оповещением делегатов о событиях экрана
 */
public abstract class BaseScreenEventDelegateManager implements ScreenEventDelegateManager {

    private Set<WeakReference<ScreenEventDelegate>> delegates = new HashSet<>();

    @Override
    public void registerDelegate(ScreenEventDelegate delegate){
        deleteNullDelegates();
        delegates.add(new WeakReference<>(delegate));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Class<ActivityResultDelegate> delegateType = ActivityResultDelegate.class;
        for(ActivityResultDelegate delegate : getDelegates(delegateType)){
            if(delegate.onActivityResult(requestCode, resultCode, data)){
                return;
            }
        }
        logUnhandled(delegateType, "requestCode=" + requestCode
                + " resultCode=" + resultCode
                + " data=" + data);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        Class<RequestPermissionsResultDelegate> delegateType = RequestPermissionsResultDelegate.class;
        for(RequestPermissionsResultDelegate delegate : getDelegates(delegateType)){
            if(delegate.onRequestPermissionsResult(requestCode, permissions, grantResults)){
                return;
            }
        }
        logUnhandled(delegateType, "requestCode="+ requestCode
                + " permissions=" + Arrays.toString(permissions)
                + " grantResults=" + Arrays.toString(grantResults));
    }

    protected <D extends ScreenEventDelegate> List<D> getDelegates(Class<? extends D> clazz) {
        return Stream.of(delegates)
                .map(Reference::get)
                .filter(delegate-> delegate != null)
                .filter(clazz::isInstance)
                .map(delegate -> (D)delegate)
                .collect(Collectors.toList());
    }

    protected void logUnhandled(Class delegateClass, String unhandledData) {
        String tag = this.getClass().getSimpleName();
        Log.e(tag, "Unhandled screen event: "+ unhandledData
                + "\nProbably you do not register delegate "+ delegateClass.getSimpleName()
                + "\nor you handle this event on fragment");
    }

    private void deleteNullDelegates() {
        delegates = Stream.of(delegates)
                .filter(delegate-> delegate.get() != null)
                .collect(Collectors.toSet());
    }
}
