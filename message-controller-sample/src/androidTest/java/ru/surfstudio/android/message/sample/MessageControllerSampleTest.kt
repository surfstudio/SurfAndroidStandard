package ru.surfstudio.android.message.sample

import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.message.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class MessageControllerSampleTest {

    @Before
    fun setUp() {
        launchActivity(MainActivityView::class.java)
    }

    @Test
    fun testMessageControllerSample() {
        clickAndCheckSnackbar(
                R.id.show_colored_snackbar,
                R.id.show_snackbar_with_duration,
                R.id.show_snackbar_with_listener
        )

        performClick(R.id.show_gravity_toast)
        checkIfToastIsVisible(R.string.toast_message)
    }

    private fun clickAndCheckSnackbar(@IdRes vararg buttonResArray: Int) {
        buttonResArray.forEach {
            performClick(it)
            checkIfSnackbarIsVisible(R.string.snackbar_message)
            performClick(R.id.close_snackbar)
        }
    }
}