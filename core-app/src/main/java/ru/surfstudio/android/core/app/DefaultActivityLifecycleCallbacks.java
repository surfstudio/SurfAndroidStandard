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
import android.app.Application;
import android.os.Bundle;

/**
 * Слушатель жизненного цикла активити по умолчанию
 *
 * @deprecated Создайте этот класс у себя на проекте если нужно. Реализация также лежит в template
 */
@Deprecated
public class DefaultActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //do nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        //do nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //do nothing
    }
}