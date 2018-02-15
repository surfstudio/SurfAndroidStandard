package ru.surfstudio.android.core.ui.base.screen.state;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;

/**
 * Предоставляет текущее состояние экрана и живой фрагмент - вью
 */

public class FragmentViewScreenState extends FragmentScreenState {
    private CoreFragmentViewInterface coreFragmentView;

    public void onDestroy() {
        super.onDestroy();
        coreFragmentView = null;
    }

    public void onCreate(Fragment fragment, CoreFragmentViewInterface coreFragmentView, @Nullable Bundle savedInstanceState) {
        super.onCreate(fragment, coreFragmentView, savedInstanceState);
        this.coreFragmentView = coreFragmentView;
    }

    @Override
    @Deprecated
    public void onCreate(Fragment fragment, CoreFragmentInterface coreFragment, @Nullable Bundle savedInstanceState) {
        throw new UnsupportedOperationException("call another onCreate");
    }

    public CoreFragmentViewInterface getCoreFragmentView() {
        return coreFragmentView;
    }
}
