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


import androidx.fragment.app.DialogFragment;

import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

/**
 * DialogNavigator работающий из активити
 */
public class DialogNavigatorForActivity extends BaseDialogNavigator {

    private ActivityProvider activityProvider;
    private ActivityViewPersistentScope activityViewPersistentScope;


    public DialogNavigatorForActivity(ActivityProvider activityProvider,
                                      ActivityViewPersistentScope activityViewPersistentScope) {
        super(activityProvider, activityViewPersistentScope);
        this.activityProvider = activityProvider;
        this.activityViewPersistentScope = activityViewPersistentScope;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(
            D fragment,
            String tag
    ) {
        fragment.show(activityViewPersistentScope, tag);
    }
}
