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
 * Базовый класс для всех инструментальных тестов примеров
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