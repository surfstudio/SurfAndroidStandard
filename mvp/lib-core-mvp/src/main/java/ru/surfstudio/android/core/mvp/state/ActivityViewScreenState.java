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
package ru.surfstudio.android.core.mvp.state;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;

/**
 * Предоставляет текущее состояние экрана и живую активити - вью
 */

public class ActivityViewScreenState extends ActivityScreenState {
    private CoreActivityViewInterface coreActivityView;

    public void onDestroy() {
        super.onDestroy();
        coreActivityView = null;
    }

    public void onCreate(FragmentActivity activity, CoreActivityViewInterface coreActivityView, @Nullable Bundle savedInstanceState) {
        super.onCreate(activity, coreActivityView, savedInstanceState);
        this.coreActivityView = coreActivityView;
    }

    @Override
    @Deprecated
    public void onCreate(FragmentActivity activity, CoreActivityInterface coreActivity, @Nullable Bundle savedInstanceState) {
        throw new UnsupportedOperationException("call another onCreate");
    }

    public CoreActivityViewInterface getCoreActivityView() {
        return coreActivityView;
    }
}
