package ru.surfstudio.android.standarddialog.sample

import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
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
}