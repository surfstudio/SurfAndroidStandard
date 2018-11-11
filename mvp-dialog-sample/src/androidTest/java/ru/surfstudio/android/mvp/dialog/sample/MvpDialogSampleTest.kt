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

@RunWith(AndroidJUnit4::class)
@SmallTest
class MvpDialogSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testMvpDialogSample() {
        //test simple dialog showing
        testSimpleDialog(
                R.id.show_simple_dialog_btn,
                intArrayOf(R.string.simple_dialog_title, R.string.simple_dialog_message),
                android.R.id.button1
        )

        //test simple bottom sheet dialog
        testSimpleDialog(
                R.id.show_simple_bottomsheet_dialog_btn,
                intArrayOf(R.string.bottom_sheet_action),
                R.id.simple_bottom_sheet_dialog_action_container
        )

        //test complex dialog showing
        performClick(R.id.show_complex_dialog_btn)
        checkViewText(R.id.value_tv, INITIAL_COMPLEX_DIALOG_VALUE.toString())
        clickAndCheck(R.id.decrease_btn, INITIAL_COMPLEX_DIALOG_VALUE - 1)
        clickAndCheck(R.id.increase_btn, INITIAL_COMPLEX_DIALOG_VALUE)
        performClick(R.id.apply_btn)
    }

    private fun testSimpleDialog(
            @IdRes showSimpleDialogBtnResId: Int,
            dialogButtons: IntArray,
            @IdRes acceptDialogBtnResId: Int
    ) {
        performClick(showSimpleDialogBtnResId)
        checkText(*dialogButtons)
        performClick(acceptDialogBtnResId)
    }

    private fun clickAndCheck(@IdRes buttonResId: Int, newValue: Int) {
        performClick(buttonResId)
        checkViewText(R.id.value_tv, newValue.toString())
    }
}