package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;

/**
 * Created by makstuev on 24.01.2018.
 */

public interface ScreenDelegateFactory {
    <A extends FragmentActivity & BaseActivityInterface> BaseActivityDelegate createBaseActivtyDelegate(A activity);

    <A extends FragmentActivity & PresenterHolderActivityCoreView> MvpActivityViewDelegate createMvpActivityViewDelegate(A activity);

    <A extends Fragment & BaseFragmentInterface> BaseFragmentDelegate createBaseFragmentDelegate(A fragment);

    <A extends Fragment & PresenterHolderFragmentCoreView> MvpFragmentViewDelegate createMvpFragmentViewDelegate(A fragment);
}
