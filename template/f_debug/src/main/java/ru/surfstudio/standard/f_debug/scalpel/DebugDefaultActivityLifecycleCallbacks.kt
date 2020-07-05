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
package ru.surfstudio.standard.f_debug.scalpel

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Слушатель жизненного цикла активити по умолчанию
 */
open class DebugDefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        //do nothing
    }

    override fun onActivityStarted(activity: Activity) {
        //do nothing
    }

    override fun onActivityResumed(activity: Activity) {
        //do nothing
    }

    override fun onActivityPaused(activity: Activity) {
        //do nothing
    }

    override fun onActivityStopped(activity: Activity) {
        //do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
        //do nothing
    }

    override fun onActivityDestroyed(activity: Activity) {
        //do nothing
    }
}