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
package ru.surfstudio.android.navigation.observer.route

import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import java.io.Serializable

/**
 * Route for activity with result [ActivityRoute]
 * You should use this route when launching system screens.
 *
 * @param <T> тип результата
</T> */
abstract class ActivityWithResultRoute<T : Serializable> : ActivityRoute(), ResultRoute<T> {

    override fun getId(): String {
        return this.javaClass.canonicalName
    }

    abstract fun parseResultIntent(resultCode: Int, resultIntent: Intent?): T
}