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
package ru.surfstudio.android.core.ui.navigation.activity.navigator;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstaller;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;

/**
 * ActivityNavigator working in Fragment.
 */
public class ActivityNavigatorForFragment extends BaseActivityNavigator {

    private FragmentProvider fragmentProvider;

    public ActivityNavigatorForFragment(ActivityProvider activityProvider,
                                        FragmentProvider fragmentProvider,
                                        ScreenEventDelegateManager eventDelegateManager) {
        this(activityProvider, fragmentProvider, eventDelegateManager, null, false);

    }

    public ActivityNavigatorForFragment(ActivityProvider activityProvider,
                                        FragmentProvider fragmentProvider,
                                        ScreenEventDelegateManager eventDelegateManager,
                                        SplitFeatureInstaller splitFeatureInstaller,
                                        Boolean isSplitFeatureModeOn) {
        super(activityProvider, eventDelegateManager, splitFeatureInstaller, isSplitFeatureModeOn);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle) {
        fragmentProvider.get().startActivityForResult(intent, requestCode, bundle);
    }
}