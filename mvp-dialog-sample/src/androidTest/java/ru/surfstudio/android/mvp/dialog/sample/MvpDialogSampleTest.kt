package ru.surfstudio.android.mvp.dialog.sample

import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.main.INITIAL_COMPLEX_DIALOG_VALUE
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.*
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkText
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkViewText

@RunWith(AndroidJUnit4::class)
@SmallTest
class MvpDialogSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testMvpDialogSample() {
        testSimpleDialog(
                R.id.show_simple_dialog_btn,
                intArrayOf(R.string.simple_dialog_title, R.string.simple_dialog_message)
        )

        testSimpleDialog(
                R.id.show_simple_bottomsheet_dialog_btn,
                intArrayOf(R.string.bottom_sheet_action),
                R.id.simple_bottom_sheet_dialog_action_container
        )

        testComplexDialog(R.id.show_complex_dialog_btn, R.id.show_complex_bottomsheet_dialog_btn)
    }

    private fun testSimpleDialog(
            @IdRes showSimpleDialogBtnResId: Int,
            @IdRes dialogTextRedList: IntArray,
            @IdRes acceptDialogBtnResId: Int = android.R.id.button1
    ) {
        performClick(showSimpleDialogBtnResId)
        checkText(*dialogTextRedList)
        performClick(acceptDialogBtnResId)
    }

    private fun testComplexDialog(@IdRes vararg showSimpleDialogBtnResIdList: Int) {
        showSimpleDialogBtnResIdList.forEach {
            performClick(it)
            checkViewText(INITIAL_COMPLEX_DIALOG_VALUE)
            clickAndCheck(R.id.decrease_btn, INITIAL_COMPLEX_DIALOG_VALUE - 1)
            clickAndCheck(R.id.increase_btn, INITIAL_COMPLEX_DIALOG_VALUE)
            performClick(R.id.apply_btn)
        }
    }

    private fun clickAndCheck(@IdRes buttonResId: Int, newValue: Int) {
        performClick(buttonResId)
        checkViewText(newValue)
    }

    private fun checkViewText(value: Int) {
        checkViewText(R.id.value_tv, value.toString())
    }
}