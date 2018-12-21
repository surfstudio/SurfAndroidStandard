package ru.surfstudio.android.loadstate.sample

import org.junit.Test
import ru.surfstudio.android.loadstate.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.DefaultRendererDemoActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class LoadStateSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testLoadStateSample() {
        performClick(R.id.activity_main_default_btn)
        checkIfActivityIsVisible(DefaultRendererDemoActivityView::class.java)
        //performClick(R.id.main_loading_btn)
    }
}