package ru.surfstudio.android.custom_scope_sample

import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.custom_scope_sample.ui.screen.main.MainActivityView

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