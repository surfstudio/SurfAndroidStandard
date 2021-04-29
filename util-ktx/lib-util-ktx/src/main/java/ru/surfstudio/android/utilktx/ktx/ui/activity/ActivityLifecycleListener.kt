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
package ru.surfstudio.android.utilktx.ktx.ui.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Слушатель жизненного цикла активити
 */
open class ActivityLifecycleListener(
        private val onActivityCreated: ((activity: Activity?, savedInstanceState: Bundle?) -> Unit)? = null,
        private val onActivityStarted: ((activity: Activity?) -> Unit)? = null,
        private val onActivityResumed: ((activity: Activity?) -> Unit)? = null,
        private val onActivityPaused: ((activity: Activity?) -> Unit)? = null,
        private val onActivityStopped: ((activity: Activity?) -> Unit)? = null,
        private val onActivityDestroyed: ((activity: Activity?) -> Unit)? = null,
        private val onActivitySaveInstanceState: ((activity: Activity?, savedInstanceState: Bundle?) -> Unit)? = null
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        onActivityCreated?.invoke(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        onActivityStarted?.invoke(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        onActivityResumed?.invoke(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        onActivityPaused?.invoke(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        onActivityStopped?.invoke(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityDestroyed?.invoke(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        onActivitySaveInstanceState?.invoke(activity, outState)
    }
}