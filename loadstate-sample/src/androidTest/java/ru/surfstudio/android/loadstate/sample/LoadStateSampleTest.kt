package ru.surfstudio.android.loadstate.sample

import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.loadstate.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.PermissionUtils
import ru.surfstudio.android.sample.common.test.utils.ShellUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class LoadStateSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    private val animationPermission = "android.permission.SET_ANIMATION_SCALE"

    private val disableAnimationsCommands = arrayOf(
            "settings put global window_animation_scale 0",
            "settings put global transition_animation_scale 0",
            "settings put global animator_duration_scale 0"
    )

    private val enableAnimationsCommands = arrayOf(
            "settings put global window_animation_scale 1",
            "settings put global transition_animation_scale 1",
            "settings put global animator_duration_scale 1"
    )

    override fun setUp() {
        super.setUp()
        PermissionUtils.grantPermissions(animationPermission)
        ShellUtils.executeCommands(*disableAnimationsCommands)
    }

    override fun tearDown() {
        super.tearDown()
        ShellUtils.executeCommands(*enableAnimationsCommands)
    }

    @Test
    fun testLoadStateSample() {
        performClick(R.id.activity_main_default_btn)
        Espresso.pressBack()
        //checkIfActivityIsVisible(DefaultRendererDemoActivityView::class.java)
        //performClick(R.id.main_loading_btn)
    }
}