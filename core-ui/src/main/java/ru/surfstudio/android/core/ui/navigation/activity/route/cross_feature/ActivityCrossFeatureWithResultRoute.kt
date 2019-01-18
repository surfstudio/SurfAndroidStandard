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
package ru.surfstudio.android.core.ui.navigation.activity.route.cross_feature

import android.content.Intent
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import java.io.Serializable

/**
 * Cross-feature navigation activity route with support of activity result delivering.
 *
 * Designed for navigation between two activities in different independent Gradle-projects.

 * @param T result type (should be [Serializable])
 *
 * @see [ActivityRoute]
 * @see [CrossFeatureRoute]
 * @see [ActivityCrossFeatureRoute]
 */
abstract class ActivityCrossFeatureWithResultRoute<T : Serializable> :
        ActivityCrossFeatureRoute(),
        SupportOnActivityResultRoute<T> {

    override fun prepareResultIntent(resultData: T): Intent {
        val i = Intent()
        i.putExtra(SupportOnActivityResultRoute.EXTRA_RESULT, resultData)
        return i
    }

    override fun parseResultIntent(resultIntent: Intent): T {
        return resultIntent.getSerializableExtra(SupportOnActivityResultRoute.EXTRA_RESULT) as T
    }

    override fun getRequestCode(): Int {
        return Math.abs(this.javaClass.canonicalName!!.hashCode() % MAX_REQUEST_CODE)
    }

}

private const val MAX_REQUEST_CODE = 32768