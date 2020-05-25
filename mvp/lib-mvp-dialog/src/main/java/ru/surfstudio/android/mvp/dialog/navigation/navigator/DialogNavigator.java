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


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.provider.Provider;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

/**
 * позволяет открывать диалоги
 */
public abstract class DialogNavigator implements Navigator, IDialogNavigator {

    private Provider<AppCompatActivity> activityProvider;
    private ScreenPersistentScope screenPersistentScope;

    public DialogNavigator(Provider<AppCompatActivity> activityProvider,
                           ScreenPersistentScope screenPersistentScope) {
        this.activityProvider = activityProvider;
        this.screenPersistentScope = screenPersistentScope;
    }

    public void show(DialogRoute dialogRoute) {
        DialogFragment dialog = dialogRoute.createFragment();
        String tag = dialogRoute.getTag();
        if (dialog instanceof CoreSimpleDialogInterface) {
            showSimpleDialog((DialogFragment & CoreSimpleDialogInterface) dialog, tag);
        } else {
            dialog.show(activityProvider.get().getSupportFragmentManager(), tag);
        }
    }

    public void dismiss(DialogRoute dialogRoute) {
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) fragmentManager
                .findFragmentByTag(dialogRoute.getTag());
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    protected abstract <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(
            D fragment,
            String tag
    );

}
