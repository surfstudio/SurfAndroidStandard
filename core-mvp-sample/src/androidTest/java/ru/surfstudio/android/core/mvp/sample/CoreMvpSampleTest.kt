package ru.surfstudio.android.core.mvp.sample

import ru.surfstudio.android.core.mvp.sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.core.mvp.sample.ui.screen.main.MainActivityView

class CoreMvpSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testOpenAnotherScreen() {
        performClick(R.id.open_another_screen_btn)
        checkIfActivityIsVisible(AnotherActivityView::class.java)
    }
}