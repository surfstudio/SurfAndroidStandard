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
package ru.surfstudio.android.sample.common.test.base

import android.app.Activity
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils
import ru.surfstudio.android.sample.common.test.utils.TextUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils

/**
 * Base class for all instrumental tests in samples
 */
@RunWith(AndroidJUnit4::class)
open class BaseSampleTest<T : Activity>(private val mainActivityClass: Class<T>) {

    @Before
    @CallSuper
    open fun setUp() {
        ActivityUtils.launchActivity(mainActivityClass)
    }

    protected fun testSimpleDialog(
            @IdRes showSimpleDialogBtnResId: Int,
            @IdRes dialogTextRedList: IntArray,
            @IdRes acceptDialogBtnResId: Int = android.R.id.button1
    ) {
        ViewUtils.performClick(showSimpleDialogBtnResId)
        TextUtils.checkText(*dialogTextRedList)
        ViewUtils.performClick(acceptDialogBtnResId)
    }
}