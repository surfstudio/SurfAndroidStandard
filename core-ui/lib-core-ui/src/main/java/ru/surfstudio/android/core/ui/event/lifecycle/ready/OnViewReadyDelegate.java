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
package ru.surfstudio.android.core.ui.event.lifecycle.ready;


import android.os.Bundle;
import android.os.PersistableBundle;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие готовности иерархии вью
 * вызовется когда вью будет проинициализирована полностью,
 * например после вызова метода {@link CoreActivityInterface#onCreate(Bundle, PersistableBundle, boolean)}
 */
public interface OnViewReadyDelegate extends ScreenEventDelegate {

    void onViewReady();
}
