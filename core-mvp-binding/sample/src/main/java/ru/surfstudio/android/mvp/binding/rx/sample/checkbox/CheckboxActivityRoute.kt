/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.mvp.binding.rx.sample.checkbox

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute

/**
 * Роут для [CheckboxActivityView]
 */
class CheckboxActivityRoute(val isCheckFirst: Boolean) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(intent.getBooleanExtra(Route.EXTRA_FIRST, false))

    override fun prepareIntent(context: Context): Intent {
        return Intent(context, CheckboxActivityView::class.java).apply {
            putExtra(Route.EXTRA_FIRST, isCheckFirst)
        }
    }
}