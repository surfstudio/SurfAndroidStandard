/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.sample.common.test

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * ViewAction which allows function call scrollTo() for NestedScrollView
 */
fun nestedScrollTo(): ViewAction = object : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
                isDescendantOfA(isAssignableFrom(NestedScrollView::class.java)),
                withEffectiveVisibility(Visibility.VISIBLE))
    }

    override fun getDescription(): String {
        return "Find parent with type " + NestedScrollView::class.java +
                " of matched view and programmatically scroll to it."
    }

    override fun perform(uiController: UiController, view: View) {
        try {
            val nestedScrollView = findFirstParentLayoutOfClass(view, NestedScrollView::class.java) as NestedScrollView?
            if (nestedScrollView != null) {
                val coordinatorLayout = findFirstParentLayoutOfClass(view, CoordinatorLayout::class.java) as CoordinatorLayout?
                if (coordinatorLayout != null) {
                    val collapsingToolbarLayout = findCollapsingToolbarLayoutChildIn(coordinatorLayout)
                    if (collapsingToolbarLayout != null) {
                        val toolbarHeight = collapsingToolbarLayout.height
                        nestedScrollView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                        nestedScrollView.dispatchNestedPreScroll(0, toolbarHeight, null, null)
                    }
                }
                nestedScrollView.scrollTo(0, view.top)
            } else {
                throw Exception("Unable to find NestedScrollView parent.")
            }
        } catch (e: Exception) {
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(e)
                    .build()
        }

        uiController.loopMainThreadUntilIdle()
    }
}

private fun findCollapsingToolbarLayoutChildIn(viewGroup: ViewGroup): CollapsingToolbarLayout? {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        if (child is CollapsingToolbarLayout) {
            return child
        } else if (child is ViewGroup) {
            return findCollapsingToolbarLayoutChildIn(child)
        }
    }
    return null
}

private fun findFirstParentLayoutOfClass(view: View, parentClass: Class<out View>): View? {
    var parent: ViewParent? = FrameLayout(view.context)
    var incrementView: ViewParent? = null
    var i = 0
    while (parent != null && parent.javaClass != parentClass) {
        parent = if (i == 0) {
            findParent(view)
        } else {
            findParent(incrementView!!)
        }
        incrementView = parent
        i++
    }
    return parent as View?
}

private fun findParent(view: View): ViewParent = view.parent

private fun findParent(view: ViewParent): ViewParent = view.parent