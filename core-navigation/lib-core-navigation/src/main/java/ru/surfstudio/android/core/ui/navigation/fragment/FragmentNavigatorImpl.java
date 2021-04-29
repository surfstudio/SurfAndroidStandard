/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.navigation.fragment;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.surfstudio.android.core.ui.FragmentContainer;
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;


public class FragmentNavigatorImpl implements FragmentNavigator {
    protected final ActivityProvider activityProvider;

    public FragmentNavigatorImpl(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    @Override
    public void add(FragmentRoute route, boolean stackable, @Transit int transition) {
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(viewContainerId, route.createFragment(), route.getTag());
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(route.getTag());
        }

        fragmentTransaction.commit();
    }

    @Override
    public void replace(FragmentRoute route, boolean stackable, @Transit int transition) {
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewContainerId, route.createFragment(), route.getTag());
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(route.getTag());
        }

        fragmentTransaction.commit();
    }

    @Override
    public boolean remove(FragmentRoute route, @Transit int transition) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        fragmentManager.beginTransaction()
                .setTransition(transition)
                .remove(fragment)
                .commit();

        return true;
    }

    @Override
    public boolean show(FragmentRoute route, @Transit int transition) {
        return toggleVisibility(route, true, transition);
    }

    @Override
    public boolean hide(FragmentRoute route, @Transit int transition) {
        return toggleVisibility(route, false, transition);
    }

    @Override
    public boolean popBackStack() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        return fragmentManager.popBackStackImmediate();
    }

    @Override
    public boolean popBackStack(@NonNull FragmentRoute route, boolean inclusive) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        boolean routeFoundInBackStack = false;
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            if (route.getTag().equals(backStack.getName())) {
                routeFoundInBackStack = true;
                break;
            }
        }

        if (!routeFoundInBackStack) {
            return false;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            Fragment backStackFragment = fragmentManager.findFragmentByTag(backStack.getName());
            if (backStackFragment == fragment) {
                break;
            }
        }

        return fragmentManager.popBackStackImmediate(route.getTag(),
                inclusive ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
    }

    @Override
    public boolean clearBackStack() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        final int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount == 0) {
            return false;
        }

        for (int i = 0; i < backStackCount; i++) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            Fragment fragment = fragmentManager.findFragmentByTag(backStack.getName());
        }

        return fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(backStackCount - 1).getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected FragmentManager getFragmentManager() {
        return activityProvider.get().getSupportFragmentManager();
    }

    @IdRes
    protected int getViewContainerIdOrThrow() {
        Object contentContainerView = activityProvider.get();
        if (contentContainerView instanceof FragmentContainer) {
            int viewContainerId = ((FragmentContainer) contentContainerView).getContentContainerViewId();
            if (viewContainerId > 0) {
                return viewContainerId;
            }
        }

        throw new IllegalStateException("Container has to have a ContentViewContainer " +
                "implementation in order to make fragment navigation");
    }

    private boolean toggleVisibility(FragmentRoute route, boolean show, @Transit int transition) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        if (show) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.hide(fragment);
        }

        fragmentTransaction.commit();
        return true;
    }
}
