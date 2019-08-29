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
package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;

import io.reactivex.Observable;
import ru.surfstudio.android.core.ui.activity.CoreActivity;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute;
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogWithResultRoute;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

/**
 * позволяет открывать диалоги
 */
public abstract class DialogNavigator extends BaseActivityResultDelegate implements Navigator {

    private ActivityProvider activityProvider;

    public DialogNavigator(ActivityProvider activityProvider,
                           ScreenEventDelegateManager screenEventDelegateManager) {
        screenEventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
    }

    public void show(DialogRoute dialogRoute) {
        DialogFragment dialog = dialogRoute.createFragment();
        if (dialog instanceof CoreSimpleDialogInterface) {
            showSimpleDialog((DialogFragment & CoreSimpleDialogInterface) dialog);
        } else {
            dialog.show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
        }
    }

    public void dismiss(DialogRoute dialogRoute) {
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) fragmentManager
                .findFragmentByTag(dialogRoute.getTag());
        dialogFragment.dismiss();
    }

    protected abstract <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D fragment);

    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends SupportOnActivityResultRoute<T>> routeClass
    ) {
        try {
            return this.observeOnActivityResult(routeClass.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("route class " + routeClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            SupportOnActivityResultRoute<T> route
    ) {
        return super.observeOnActivityResult(route);
    }

    public <T extends Serializable> void showForResult(
            FragmentRoute currentRoute,
            DialogWithResultRoute<T> dialogRoute
    ) {
        if (!super.isObserved(dialogRoute)) {
            throw new IllegalStateException("route class " + dialogRoute.getClass().getSimpleName()
                    + " must be registered by method DialogNavigator#observeResult");
        }

        DialogFragment dialog = dialogRoute.createFragment();
        Fragment currentFragment = activityProvider.get().getSupportFragmentManager().findFragmentByTag(currentRoute.getTag());
        if (currentFragment != null) {
            dialog.setTargetFragment(currentFragment, dialogRoute.getRequestCode());
        }

        dialog.show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
    }

    public <T extends Serializable> void showForResultFromActivity(
            DialogWithResultRoute<T> dialogRoute
    ) {
        if (!super.isObserved(dialogRoute)) {
            throw new IllegalStateException("route class " + dialogRoute.getClass().getSimpleName()
                    + " must be registered by method DialogNavigator#observeResult");
        }

        dialogRoute.createFragment().show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
    }

    public <T extends Serializable> boolean dismissWithResult(
            DialogWithResultRoute<T> dialogRoute,
            T result,
            boolean success
    ) {
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogRoute.getTag());
        if (dialogFragment == null) return false;
        Fragment target = dialogFragment.getTargetFragment();
        Intent resultIntent = dialogRoute.prepareResultIntent(result);
        if (target != null) {
            target.onActivityResult(
                    dialogRoute.getRequestCode(),
                    success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                    resultIntent
            );
        } else {
            ((CoreActivity) activityProvider.get()).handleActivityResult(
                    dialogRoute.getRequestCode(),
                    success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                    resultIntent
            );
        }
        dialogFragment.dismiss();
        return true;
    }
}
