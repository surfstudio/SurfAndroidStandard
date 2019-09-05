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

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.FragmentRouteInterface

/**
 * Cross-feature navigation Fragment route with parameters for different independent Gradle-projects.
 *
 * @see [FragmentCrossFeatureRoute]
 * @see [FragmentRouteInterface]
 */
abstract class FragmentCrossFeatureWithParamsRoute : FragmentCrossFeatureRoute, FragmentRouteInterface {

    constructor() {
        //empty
    }

    @Suppress("ConvertSecondaryConstructorToPrimary", "UNUSED_PARAMETER")
    constructor(bundle: Bundle) : this()

    override fun createFragment(): Fragment {
        return super.createFragment().apply {
            arguments = prepareBundle()
        }
    }
}