package ru.surfstudio.android.core.ui.navigation.fragment.route;

import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.navigation.Route;

/**
 * базовый маршрут для открытия фрагмента
 * см {@link Route}
 */
public abstract class FragmentRoute implements Route {

    protected abstract Class<? extends Fragment> getFragmentClass();

    public String getTag() {
        return getFragmentClass().getCanonicalName();
    }

    public Fragment createFragment() {
        try {
            return getFragmentClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
