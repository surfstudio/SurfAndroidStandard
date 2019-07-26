package ru.surfstudio.android.core.ui.sample

import ru.surfstudio.android.core.ui.sample.ui.screen.main.MainActivityView

class CoreUiSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testCoreUiSample() {
        checkIfSnackbarIsVisible(R.string.snackbar_message)
    }
}