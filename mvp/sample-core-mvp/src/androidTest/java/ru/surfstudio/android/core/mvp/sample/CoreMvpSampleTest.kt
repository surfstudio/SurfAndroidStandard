package ru.surfstudio.android.core.mvp.sample

import org.junit.Test
import ru.surfstudio.android.core.mvp.sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.core.mvp.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class CoreMvpSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testOpenAnotherScreen() {
        performClick(R.id.open_another_screen_btn)
        checkIfActivityIsVisible(AnotherActivityView::class.java)
    }
}