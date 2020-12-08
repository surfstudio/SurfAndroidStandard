/*
  Copyright (c) 2019-present, SurfStudio LLC, Maxim Tuev.

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

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.logger.Logger

/**
 * Cross-feature navigation Fragment route for different independent Gradle-projects.
 *
 * @see [FragmentRoute]
 * @see [CrossFeatureRoute]
 */
abstract class FragmentCrossFeatureRoute : CrossFeatureRoute, FragmentRoute() {

    @Suppress("UNCHECKED_CAST")
    override fun getFragmentClass(): Class<out Fragment>? {
        try {
            return Class.forName(targetClassPath()) as? Class<out Fragment> ?: return null
        } catch (e: ClassNotFoundException) {
            Logger.e("Fragment with the following classpath was not found in the current " +
                    "application: ${targetClassPath()}. If this fragment is the part of Dynamic Feature, " +
                    "please check if this Dynamic Feature is downloaded and installed on the device" +
                    "successfully.")
        }
        return null
    }
}