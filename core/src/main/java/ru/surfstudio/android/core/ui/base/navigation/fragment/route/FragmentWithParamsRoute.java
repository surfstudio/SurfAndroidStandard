package ru.surfstudio.android.core.ui.base.navigation.fragment.route;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.navigation.Route;

/**
 * см {@link Route}
 */
public abstract class FragmentWithParamsRoute extends FragmentRoute {

    public FragmentWithParamsRoute(){
    }

    public FragmentWithParamsRoute(Bundle bundle){

    }

    protected abstract Bundle prepareBundle();

    @Override
    public Fragment createFragment(){
        Fragment fragment = super.createFragment();
        fragment.setArguments(prepareBundle());
        return fragment;
    }
}
