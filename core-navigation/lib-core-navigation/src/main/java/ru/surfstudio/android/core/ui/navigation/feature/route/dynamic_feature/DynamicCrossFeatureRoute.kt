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
package ru.surfstudio.android.core.ui.navigation.feature.route.dynamic_feature

import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureRoute

/**
 * Interface for Dynamic Feature navigation route.
 *
 * Should be used for routing to Activity from the Dynamic Feature module.
 *
 * For using it override [targetClassPath] method and return full classpath of the target
 * feature starting point (e.g. activity). Also you need to specify [splitName] which should be the
 * same as the Dynamic Feature Gradle-projects' name.
 *
 * Read more: [https://developer.android.com/studio/projects/dynamic-delivery]
 */
@Deprecated("Используйте новую навигацию")
interface DynamicCrossFeatureRoute : CrossFeatureRoute {

    /**
     * @return name of the Dynamic Feature which the target starting point leads to plus names of
     * any other Feature Modules to download and install it in the same batch if necessary
     *
     * IMPORTANT NOTE: `splitName` value should be the same as the feature Gradle-projects' name!
     */
    fun splitNames(): List<String>
}