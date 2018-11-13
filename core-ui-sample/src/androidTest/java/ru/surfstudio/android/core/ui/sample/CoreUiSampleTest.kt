package ru.surfstudio.android.core.ui.sample

import org.junit.Test
import ru.surfstudio.android.core.ui.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfSnackbarIsVisible

class CoreUiSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testCoreUiSample() {
        checkIfSnackbarIsVisible(R.string.snackbar_message)
    }
}