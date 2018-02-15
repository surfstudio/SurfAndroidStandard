package ru.surfstudio.android.core.ui.base.screen.state;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by makstuev on 25.01.2018.
 */

public class FragmentScreenState extends BaseScreenState {
    private Fragment fragment;

    public void onCreate(Fragment fragment, Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        this.fragment = fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment = null;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
