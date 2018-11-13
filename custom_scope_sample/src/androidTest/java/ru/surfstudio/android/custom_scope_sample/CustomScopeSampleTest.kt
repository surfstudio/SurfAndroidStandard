package ru.surfstudio.android.custom_scope_sample

import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityView
import ru.surfstudio.android.custom_scope_sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.utils.*
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

@RunWith(AndroidJUnit4::class)
@SmallTest
class CustomScopeSampleTest {

    private val emailText = "email"

    @Before
    fun setUp() {
        Intents.init()
        launchActivity(MainActivityView::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testCustomScope() {
        performClick(R.id.open_another_screen_btn)
        checkIfActivityIsVisible(AnotherActivityView::class.java)
        checkAndInputText(R.id.another_screen_et, R.string.another_screen_email, true, emailText)
        Espresso.pressBack()
        checkIfActivityIsVisible(MainActivityView::class.java)
        checkIfToastIsVisible(emailText)
    }
}