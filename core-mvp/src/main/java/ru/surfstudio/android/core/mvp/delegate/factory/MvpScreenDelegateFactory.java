package ru.surfstudio.android.core.mvp.delegate.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;

/**
 * Фабрика делегатов MVP экранов, создана для того чтобы была возможность применить некоторую
 * логику ко всем MVP экранам конкретного приложения
 */

public interface MvpScreenDelegateFactory {
    <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate createActivityViewDelegate(A activity);

    <F extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate createFragmentViewDelegate(F fragment);
}
