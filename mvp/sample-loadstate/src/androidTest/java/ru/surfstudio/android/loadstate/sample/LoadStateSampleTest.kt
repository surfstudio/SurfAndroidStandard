package ru.surfstudio.android.loadstate.sample

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.loadstate.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.DefaultRendererDemoActivityView
import ru.surfstudio.android.loadstate.sample.ui.screen.stubs.RendererWithStubsDemoActivityView
import ru.surfstudio.android.sample.common.test.ElapsedTimeIdlingResource
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.IdlingUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class LoadStateSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

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
    fun testLoadStateSample() {
        performClick(R.id.activity_main_default_btn)
        checkIfActivityIsVisible(DefaultRendererDemoActivityView::class.java)
        performClick(R.id.main_loading_btn, R.id.tr_loading_btn, R.id.none_btn, R.id.custom_btn)

        performClickAndCheckToastMessage(R.id.empty_btn, R.string.empty_state_toast_msg)
        performClickAndCheckToastMessage(R.id.error_btn, R.string.error_state_toast_msg)
        Espresso.pressBack()

        performClick(R.id.activity_main_stubs_btn)
        checkIfActivityIsVisible(RendererWithStubsDemoActivityView::class.java)
        performClick(R.id.main_loading_btn, R.id.none_btn, R.id.error_btn, R.id.retry_btn)
        checkIfToastIsVisible(R.string.retry_toast_msg)
    }

    private fun performClickAndCheckToastMessage(
            @IdRes loadStateBtnResId: Int,
            @StringRes toastResId: Int
    ) {
        performClick(loadStateBtnResId)
        val idlingResource = ElapsedTimeIdlingResource(250L)
        IdlingUtils.registerIdlingResource(idlingResource)
        performClick(R.id.placeholder_first_btn)
        checkIfToastIsVisible(toastResId)
        IdlingUtils.unregisterIdlingResource(idlingResource)
    }
}