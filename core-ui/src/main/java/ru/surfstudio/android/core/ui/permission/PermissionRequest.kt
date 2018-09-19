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

import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute

private const val MAX_REQUEST_CODE = 32768

/**
 * Базовый класс запроса Runtime Permissions
 */
abstract class PermissionRequest {

    abstract val permissions: Array<String>

    val requestCode: Int
        get() = (this.javaClass.canonicalName.hashCode() and 0x7fffffff) % MAX_REQUEST_CODE

    var showPermissionRational: Boolean = false
        protected set

    var permissionRationalStringRes: Int? = null
        protected set

    var permissionRationalRoute: ActivityWithResultRoute<*>? = null
        protected set
}