package ru.surfstudio.android.core.ui.delegate.fragment;

import android.os.Build;
import android.support.v4.app.BackstackAccessor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

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
