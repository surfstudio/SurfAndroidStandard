package ru.surfstudio.android.core.ui.base.screen.state;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;

/**
 * Предоставляет текущее состояние экрана и живой фрагмент - контейнер
 */

public class FragmentScreenState extends BaseScreenState {
    private Fragment fragment;
    private CoreFragmentInterface coreFragment;

    public void onDestroy() {
        super.onDestroy();
        fragment = null;
        coreFragment = null;
    }

    public void onCreate(Fragment fragment, CoreFragmentInterface coreFragment, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragment = fragment;
        this.coreFragment = coreFragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public CoreFragmentInterface getCoreFragment() {
        return coreFragment;
    }
}
