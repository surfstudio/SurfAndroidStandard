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

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.allOf

/**
 * Utils for checking a given text visibility on the screen
 */
object TextUtils {

    /**
     * Function for checking if text of view which belongs to another view
     * is equal to given string resource
     */
    fun checkViewText(@IdRes viewResId: Int, @IdRes parentViewResId: Int, @StringRes textResId: Int) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(withText(textResId)))
                .check(matches(isDisplayed()))
    }

    /**
     * Function for checking if text of view which belongs to another view
     * is equal to given string
     */
    fun checkViewText(@IdRes viewResId: Int, @IdRes parentViewResId: Int, text: String) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(withText(text)))
                .check(matches(isDisplayed()))
    }

    /**
     * Function for checking if text of view is equal to given string resource
     */
    fun checkViewText(@IdRes viewResId: Int, @StringRes textResId: Int) {
        onView(withId(viewResId))
                .check(matches(withText(textResId)))
                .check(matches(isDisplayed()))
    }

    /**
     * Function for checking if text of view is equal to given string
     */
    fun checkViewText(@IdRes viewResId: Int, text: String) {
        onView(withId(viewResId))
                .check(matches(withText(text)))
                .check(matches(isDisplayed()))
    }

    /**
     * Function which checks if the screen contains a view with given string resource
     */
    fun checkText(@IdRes vararg textResIdList: Int) {
        textResIdList.forEach {
            onView(withText(it))
                    .check(matches(isDisplayed()))
        }
    }

    /**
     * Function which checks if the screen contains a view with given string
     */
    fun checkText(vararg textList: String) {
        textList.forEach {
            onView(withText(it))
                    .check(matches(isDisplayed()))
        }
    }

    /**
     * Function which checks if the text or hint of view are equal to given string resource
     * and inputs a new value
     *
     * @param viewResId view ID which value will be checked and changed
     * @param oldTextResId string resource ID which value should be equal to hint or text of view
     * @param isHint flag which shows which view property should be checked (hint or text)
     * @param newText a new text value for view
     */
    fun checkAndInputText(
            @IdRes viewResId: Int,
            @StringRes oldTextResId: Int,
            isHint: Boolean,
            newText: String
    ) {
        onView(withId(viewResId))
                .check(matches(
                        if (isHint)
                            withHint(oldTextResId)
                        else
                            withText(oldTextResId)))
                .perform(typeText(newText), closeSoftKeyboard())
    }

    /**
     * Function which returns a string resource value
     */
    fun getString(@StringRes stringResId: Int): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.getString(stringResId)
    }
}