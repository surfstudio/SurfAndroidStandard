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
package ru.surfstudio.android.mvp.widget.delegate

import android.view.View
import android.view.ViewParent
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope

/**
 * ищет родительский PersistentScope для WidgetView
 */

class ParentPersistentScopeFinder(private val child: View) {

    fun find(): ScreenPersistentScope? {
        var parentScope: ScreenPersistentScope? = null
        val activity = unwrapContext(child.context, FragmentActivity::class.java)

        val fragments = activity.supportFragmentManager.fragments
        var parent: ViewParent? = child.parent
        while (parent != null && parentScope == null) {
            for (fragment in fragments) {
                if (fragment.view != null
                        && fragment.view === parent
                        && fragment is CoreFragmentInterface) {
                    parentScope = (fragment as CoreFragmentInterface).persistentScope
                }
            }
            parent = parent.parent
        }
        if (parentScope == null) {
            parentScope = (activity as CoreActivityInterface).persistentScope
        }
        return parentScope
    }
}
