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
package ru.surfstudio.android.core.ui.event.result;


import android.content.Intent;

import java.io.Serializable;

import ru.surfstudio.android.core.ui.navigation.ActivityRouteInterface;

/**
 * интерфейс для Route, работающего через onActivityResult
 *
 * @param <T> тип данных результата, переданного в Intent
 */
public interface SupportOnActivityResultRoute<T extends Serializable> extends ActivityRouteInterface {
    String EXTRA_RESULT = "extra_result";

    Intent prepareResultIntent(T resultData);

    T parseResultIntent(Intent resultIntent);

    int getRequestCode();
}
