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
package ru.surfstudio.android.sample.common.test.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * Utils for [RecyclerView] tests
 */
object RecyclerViewUtils {

    /**
     * Function which clicks a RecyclerView's element with given position
     */
    fun performItemClick(@IdRes recyclerViewResId: Int, position: Int) {
        onView(withId(recyclerViewResId))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    /**
     * Function which scrolls a RecyclerView to given position
     */
    fun scrollTo(@IdRes recyclerViewResId: Int, position: Int) {
        onView(withId(recyclerViewResId))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    /**
     * Function which scrolls a RecyclerView to element with given title
     */
    fun scrollTo(@IdRes recyclerViewResId: Int, itemTitle: String) {
        onView(withId(recyclerViewResId))
                .perform(scrollTo<RecyclerView.ViewHolder>(
                        hasDescendant(withText(itemTitle)))
                )
    }

    /**
     * Function which scroll a RecyclerView to the end
     */
    fun scrollToBottom(@IdRes recyclerViewResId: Int) {
        onView(withId(recyclerViewResId))
                .perform(ScrollToBottomAction())
    }

    class ScrollToBottomAction : ViewAction {
        override fun getDescription(): String {
            return "scroll RecyclerView to bottom"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount
            val position = itemCount?.minus(1) ?: 0
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}