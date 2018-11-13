package ru.surfstudio.android.standarddialog.sample

import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkText
import ru.surfstudio.android.sample.common.test.utils.performClick
import ru.surfstudio.android.standarddialog.sample.ui.screen.main.MainActivityView

@RunWith(AndroidJUnit4::class)
@SmallTest
class StandardDialogSampleTest {

    private val firstDialogTextResArray = intArrayOf(
            R.string.first_dialog_title,
            R.string.first_dialog_message
    )

    private val secondDialogTextResArray = intArrayOf(
            R.string.second_dialog_title,
            R.string.second_dialog_message
    )

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

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