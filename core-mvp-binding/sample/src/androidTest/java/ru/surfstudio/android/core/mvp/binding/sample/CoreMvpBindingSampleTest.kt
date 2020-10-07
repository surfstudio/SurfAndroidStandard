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

package ru.surfstudio.android.core.mvp.binding.sample

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Test
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class CoreMvpBindingSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    @After
    @CallSuper
    fun tearDown() {
        AnimationUtils.enableAnimations()
    }

    @Test
    fun testClickActions() {
        performClick(
                "1",
                R.id.pane_1,
                R.id.pane_2,
                R.id.pane_3,
                R.id.pane_4,
                R.id.pane_5,
                R.id.pane_6,
                R.id.pane_7,
                R.id.pane_8,
                R.id.pane_9
        )
        performClick(R.id.easy_win_btn)
        checkIfToastIsVisible(R.string.win_message)
        performClick(R.id.unbind_btn)
    }
}