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
package ru.surfstudio.android.core.ui.delegate.fragment;

import android.os.Build;

import androidx.fragment.app.BackstackAccessor;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ru.surfstudio.android.core.ui.delegate.base.CompletelyDestroyChecker;

/**
 * проверяет что фрагмент полностю уничтожена
 */

public class FragmentCompletelyDestroyChecker implements CompletelyDestroyChecker {
    private Fragment fragment;

    public FragmentCompletelyDestroyChecker(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public boolean check() {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            if (activity.isChangingConfigurations()) {
                return false;
            }

            if (activity.isFinishing()) {
                return true;
            }
        }

        // See https://github.com/sockeqwe/mosby/blob/master/utils-fragment/src/main/java/android/support/v4/app/BackstackAccessor.java
        if (BackstackAccessor.isFragmentOnBackStack(fragment)) {
            return false;
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        boolean anyParentIsRemoving = false;

        if (Build.VERSION.SDK_INT >= 17) {
            Fragment parent = fragment.getParentFragment();
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving();
                parent = parent.getParentFragment();
            }
        }

        return fragment.isRemoving() || anyParentIsRemoving;
    }
}
