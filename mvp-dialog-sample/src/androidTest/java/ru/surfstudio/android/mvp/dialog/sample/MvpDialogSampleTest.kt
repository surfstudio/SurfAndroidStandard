package ru.surfstudio.android.mvp.dialog.sample

import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class MvpDialogSampleTest {

    @IdRes
    private val acceptButtonIdRes = android.R.id.button1

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testMvpDialogSample() {
        //test simple dialog showing
        performClick(R.id.show_simple_dialog_btn)
        checkText(R.string.simple_dialog_title, R.string.simple_dialog_message)
        performClick(acceptButtonIdRes)

        //test simple bottom sheet dialog
        performClick(R.id.show_simple_bottomsheet_dialog_btn)
        checkText(R.string.bottom_sheet_action)
        performClick(R.id.simple_bottom_sheet_dialog_action_container)
    }
}