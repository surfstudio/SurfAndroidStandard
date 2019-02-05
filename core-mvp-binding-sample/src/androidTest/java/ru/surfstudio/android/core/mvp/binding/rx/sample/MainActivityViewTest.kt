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

package ru.surfstudio.android.core.mvp.binding.rx.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.mvp.binding.rx.sample.MainActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.checkbox.CheckboxActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.EAMainActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination.PaginationActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class MainActivityViewTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testMvpRxSample() {
        repeat(2) { performClick(R.id.main_inc_btn) }
        performClick(R.id.main_dec_btn)
        onView(withId(R.id.main_counter_tv)).check(matches(withText("1")))

        performClick(R.id.dialog_sample_btn)
        onView(withText("Sample text")).check(matches(isDisplayed()))
        pressBack()

        performClick(R.id.checkbox_sample_btn)
        checkIfActivityIsVisible(CheckboxActivityView::class.java)
        pressBack()

        performClick(R.id.easy_adapter_sample_btn)
        checkIfActivityIsVisible(EAMainActivityView::class.java)

        onView(withText("Commercial")).perform(click())
        checkIfActivityIsVisible(PaginationActivityView::class.java)
        pressBack()
    }
}