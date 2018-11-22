package ru.surfstudio.android.custom_view_sample

import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsVisible

class CustomViewSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testCustomViewSample() {
        checkIfViewIsVisible(R.id.placeholder_view)
        performClick(R.id.change_state_btn)
    }
}