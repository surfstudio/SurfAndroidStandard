package ru.surfstudio.android.custom_scope_sample

import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.custom_scope_sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.TextUtils.checkAndInputText
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class CustomScopeSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    private val emailText = "email"

    @Test
    fun testCustomScopeSample() {
        performClick(R.id.open_another_screen_btn)
        checkIfActivityIsVisible(AnotherActivityView::class.java)
        checkAndInputText(R.id.another_screen_et, R.string.another_screen_email, true, emailText)
        Espresso.pressBack()
        checkIfActivityIsVisible(MainActivityView::class.java)
        checkIfToastIsVisible(emailText)
    }
}