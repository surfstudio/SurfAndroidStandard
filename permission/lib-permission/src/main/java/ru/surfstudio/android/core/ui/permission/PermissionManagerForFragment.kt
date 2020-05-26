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
package ru.surfstudio.android.core.ui.permission

import android.content.SharedPreferences
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider

/**
 * PermissionManager, работающий из фрагмента.
 */
class PermissionManagerForFragment(
        eventDelegateManager: ScreenEventDelegateManager,
        activityProvider: ActivityProvider,
        activityNavigator: ActivityNavigator,
        sharedPreferences: SharedPreferences,
        private val fragmentProvider: FragmentProvider
) : PermissionManager(eventDelegateManager, activityProvider, activityNavigator, sharedPreferences) {

    override fun performPermissionRequest(permissionRequest: PermissionRequest) =
            fragmentProvider.get().requestPermissions(permissionRequest.permissions, permissionRequest.requestCode)
}
