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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.surfstudio.android.core.ui.FragmentContainer;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.provider.Provider;

/**
 * позволяет осуществлять навигацияю между фрагментами внутри фрагмента
 * Используется ChildFragmentManager
 * <p>
 * Изначально ядром не поставляется, поскольку не должно быть кейсов его использования,
 * но класс оставлен на всякий случай =)
 */
public class ChildFragmentNavigator extends FragmentNavigator {
    private final FragmentProvider fragmentProvider;

    public ChildFragmentNavigator(Provider<AppCompatActivity> activityProvider,
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
