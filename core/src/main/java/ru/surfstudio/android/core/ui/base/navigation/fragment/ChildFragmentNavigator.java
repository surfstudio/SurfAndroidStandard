package ru.surfstudio.android.core.ui.base.navigation.fragment;


import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.screen.view.FragmentContainer;

/**
 * позволяет осуществлять навигацияю между фрагментами внутри фрагмента
 * Используется ChildFragmentManager
 *
 * Изначально ядром не поставляется, поскольку не должно быть кейсов его использования,
 * но класс оставлен на всякий случай =)
 */
public class ChildFragmentNavigator extends FragmentNavigator {
    private final FragmentProvider fragmentProvider;

    public ChildFragmentNavigator(ActivityProvider activityProvider,
                                  FragmentProvider fragmentProvider) {
        super(activityProvider);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected FragmentManager getFragmentManager() {
        Fragment fragment = fragmentProvider.get();
        while (!(fragment instanceof FragmentContainer)) {
            Fragment parent = fragment.getParentFragment();
            if (parent == null) {
                break;
            }

            fragment = parent;
        }
        return fragment.getChildFragmentManager();
    }

    @IdRes
    @Override
    protected int getViewContainerIdOrThrow() {
        Fragment fragment = fragmentProvider.get();
        while (!(fragment instanceof FragmentContainer)) {
            Fragment parent = fragment.getParentFragment();
            if (parent == null) {
                break;
            }

            fragment = parent;
        }

        if (fragment instanceof FragmentContainer) {
            int viewContainerId = ((FragmentContainer) fragment).getContentContainerViewId();
            if (viewContainerId > 0) {
                return viewContainerId;
            }
        }

        throw new IllegalStateException("Container has to have a ContentViewContainer " +
                "implementation in order to make fragment navigation");
    }
}
