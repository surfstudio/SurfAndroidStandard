package ru.surfstudio.android.core.ui.base.screen.delegate.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.widget.WidgetViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * Created by makstuev on 24.01.2018.
 */

public interface ScreenDelegateFactory {
    <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate createActivityDelegate(A activity);

    <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate createActivityViewDelegate(A activity);

    <F extends Fragment & CoreFragmentInterface> FragmentDelegate createFragmentDelegate(F fragment);

    <F extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate createFragmentViewDelegate(F fragment);

    <W extends View & CoreWidgetViewInterface> WidgetViewDelegate createWidgetViewDelegate(W widget);
}
