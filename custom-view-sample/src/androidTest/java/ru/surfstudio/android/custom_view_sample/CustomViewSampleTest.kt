package ru.surfstudio.android.custom_view_sample

import org.junit.Test
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class CustomViewSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    override fun tearDown() {
        super.tearDown()
        AnimationUtils.enableAnimations()
    }

    @Test
    fun testCustomViewSample() {
        checkIfActivityIsVisible(MainActivity::class.java)
        performClick(R.id.change_state_btn)
    }
}