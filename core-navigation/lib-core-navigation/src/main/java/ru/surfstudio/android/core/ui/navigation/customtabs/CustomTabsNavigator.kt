/*
  Copyright (c) 2020-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.navigation.customtabs

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.ui.navigation.Navigator

/**
 * Класс для навигации через Chrome Custom Tabs.
 */
class CustomTabsNavigator(private val activity: Activity) : Navigator {

    /**
     * Открыть url в Custom Chrome Tabs. Если не получилось - то в любом другом браузере.
     */
    fun openLink(route: OpenUrlRoute) {
        val linkUri = buildUri(route.url)
        val customTabsIntent = buildCustomTabsIntent(route.toolbarColorId, route.secondaryToolbarColorId)

        try {
            customTabsIntent.launchUrl(activity, linkUri)
        } catch (e: Throwable) {
            val browserIntent = Intent(ACTION_VIEW, linkUri)
            activity.startActivity(browserIntent)
        }
    }

    private fun buildUri(url: String): Uri {
        var uri = Uri.parse(url)
        if (uri.scheme == null) {
            uri = uri.buildUpon().scheme("http").build()
        }
        return uri
    }

    private fun buildCustomTabsIntent(@ColorRes toolbarColorId: Int, @ColorRes secondaryToolbarColorId: Int): CustomTabsIntent {
        return CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(activity, toolbarColorId))
                .setSecondaryToolbarColor(ContextCompat.getColor(activity, secondaryToolbarColorId))
                .build()
    }
}
