/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Smirnov.

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
package ru.surfstudio.android.core.ui.navigation.activity.route;

import android.content.Intent;

import ru.surfstudio.android.core.ui.navigation.NoScreenDataStub;

/**
 * см {@link ActivityRoute}
 *
 * Route с результатом без данных
 * (в кач-ве данных заглушка)
 */
public abstract class ActivityWithResultNoDataRoute extends ActivityWithResultRoute<NoScreenDataStub> {

    @Override
    public Intent prepareResultIntent(NoScreenDataStub resultData) {
        return new Intent();
    }

    @Override
    public NoScreenDataStub parseResultIntent(Intent resultIntent) {
        return NoScreenDataStub.INSTANCE;
    }
}
