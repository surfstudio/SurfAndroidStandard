package ru.surfstudio.android.message.sample

import androidx.annotation.IdRes
import org.junit.Test
import ru.surfstudio.android.message.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfSnackbarIsVisible
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class MessageControllerSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

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