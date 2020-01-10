/*
    Copyright 2016 Brandon Gogetap

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
package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.view.View
import android.widget.FrameLayout

internal object Preconditions {

    fun <T> checkNotNull(item: T?, message: String): T {
        if (item == null) {
            throw NullPointerException(message)
        }
        return item
    }

    fun validateParentView(recyclerView: View?) {
        val parentView = recyclerView?.parent as View
        if (parentView !is FrameLayout && parentView !is CoordinatorLayout) {
            throw IllegalArgumentException("RecyclerView parent must be either a FrameLayout or CoordinatorLayout")
        }
    }
}