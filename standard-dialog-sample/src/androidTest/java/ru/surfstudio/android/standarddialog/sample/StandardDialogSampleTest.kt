package ru.surfstudio.android.standarddialog.sample

import androidx.annotation.IdRes
import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.standarddialog.sample.ui.screen.main.MainActivityView

class StandardDialogSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    private val firstDialogTextResArray = intArrayOf(
            R.string.first_dialog_title,
            R.string.first_dialog_message
    )

    private val secondDialogTextResArray = intArrayOf(
            R.string.second_dialog_title,
            R.string.second_dialog_message
    )

    @Test
    fun testStandardDialogSample() {
        testSimpleDialog(R.id.first_btn, firstDialogTextResArray)
        testSimpleDialog(R.id.second_btn, secondDialogTextResArray)
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
}