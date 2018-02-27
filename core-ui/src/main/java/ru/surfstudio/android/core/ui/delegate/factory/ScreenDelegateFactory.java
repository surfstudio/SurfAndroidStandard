package ru.surfstudio.android.core.ui.delegate.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;

/**
 * Фабрика делегатов экранов, создана для того чтобы была возможность применить некоторую
 * логику ко всем экранам конкретного приложения
 */

public interface ScreenDelegateFactory {
    <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate createActivityDelegate(A activity);

    <F extends Fragment & CoreFragmentInterface> FragmentDelegate createFragmentDelegate(F fragment);
}
