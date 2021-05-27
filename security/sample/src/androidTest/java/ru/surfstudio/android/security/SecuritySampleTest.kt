package ru.surfstudio.android.security

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Test
import ru.surfstudio.android.sample.common.test.ElapsedTimeIdlingResource
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.AnimationUtils
import ru.surfstudio.android.sample.common.test.utils.IdlingUtils
import ru.surfstudio.android.sample.common.test.utils.TextUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.security.sample.ui.screen.pin.CreatePinActivityView

private const val PINT_TEXT = "1234"
private const val LONG_DURATION_TIMEOUT: Long = 4000

class SecuritySampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    override fun setUp() {
        super.setUp()
        AnimationUtils.grantScaleAnimationPermission()
        AnimationUtils.disableAnimations()
    }

    @After
    @CallSuper
    fun tearDown() {
        AnimationUtils.enableAnimations()
    }

    @Test
    fun checkRootTest() {
        performClick(R.id.check_root_btn)
        VisibilityUtils.checkIfToastIsVisible(R.string.no_root_message)
    }

    @Test
    fun signInPinTest() {
        TextUtils.checkAndInputText(R.id.api_key_et, R.string.empty_string, false, PINT_TEXT)
        performClick(R.id.sign_in_btn)
        checkIfActivityIsVisible(CreatePinActivityView::class.java)

        TextUtils.checkAndInputText(R.id.enter_pin_et, R.string.empty_string, false, PINT_TEXT)
        performClick(R.id.enter_pin_btn)
        VisibilityUtils.checkIfToastIsVisible(R.string.pin_created_message)

        // Ожидаем, пока показывается toast
        val idlingResource = ElapsedTimeIdlingResource(LONG_DURATION_TIMEOUT)
        IdlingUtils.registerIdlingResource(idlingResource)

        TextUtils.checkAndInputText(R.id.enter_pin_et, R.string.empty_string, false, PINT_TEXT)
        performClick(R.id.get_api_key_btn)
        VisibilityUtils.checkIfToastIsVisible(PINT_TEXT)
    }
}