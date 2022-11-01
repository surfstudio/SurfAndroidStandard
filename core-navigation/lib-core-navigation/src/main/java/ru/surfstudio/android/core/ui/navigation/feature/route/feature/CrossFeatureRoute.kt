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
package ru.surfstudio.android.core.ui.navigation.feature.route.feature

import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Interface for cross-feature navigation route.
 *
 * Should be used for routing between two activities from different independent Gradle-projects.
 *
 * For using it just override [targetClassPath] method and specify full classpath of the target
 * feature starting point (e.g. activity).
 */
interface CrossFeatureRoute : Route {

    /**
     * @return target starting point full classpath (e.g. "com.name.app.feature.ActivityName")
     */
    fun targetClassPath(): String
}