/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

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
package ru.surfstudio.android.core.app;

import android.app.Activity;

import androidx.annotation.Nullable;

/**
 * Содержит активную (отображаемую) активти
 *
 * @deprecated перенесен на модуль activity-holder
 */
@Deprecated
public class ActiveActivityHolder {

    private Activity activity;

    public ActiveActivityHolder() {
        //do nothing
    }

    public void setActivity(Activity activeActivity) {
        this.activity = activeActivity;
    }

    public void clearActivity() {
        this.activity = null;
    }

    @Nullable
    public Activity getActivity() {
        return activity;
    }

    public boolean isExist() {
        return activity != null;
    }
}