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
@file:Suppress("unused")

package ru.surfstudio.android.core.ui.navigation.feature.route.feature

import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import java.io.Serializable

/**
 * Cross-feature navigation Activity route with support of Activity result delivering and parameters.
 *
 * Designed for navigation between two Activities in different independent Gradle-projects.

 * @param T result type (should be [Serializable])
 *
 * @see [ActivityRoute]
 * @see [CrossFeatureRoute]
 * @see [ActivityCrossFeatureRoute]
 */
abstract class ActivityCrossFeatureWithParamsAndResultRoute<T : Serializable> :
        ActivityCrossFeatureWithResultRoute<T> {

    constructor() {
        //empty
    }

    @Suppress("ConvertSecondaryConstructorToPrimary", "UNUSED_PARAMETER")
    constructor(intent: Intent) : this()
}