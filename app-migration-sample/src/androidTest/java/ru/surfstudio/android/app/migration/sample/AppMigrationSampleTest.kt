package ru.surfstudio.android.app.migration.sample

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.SplashActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.TRANSITION_DELAY_MS

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppMigrationSampleTest {

    @Before
    fun setUp() {
        Intents.init()
        ActivityScenario.launch(SplashActivityView::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testApplicationLaunch() {
        checkIfActivityIsVisible(SplashActivityView::class.java)

        // wait until splash screen is gone
        val idlingResource = ElapsedTimeIdlingResource(TRANSITION_DELAY_MS)
        registerIdlingResource(idlingResource)

        checkIfActivityIsVisible(MainActivityView::class.java)
        unregisterIdlingResource(idlingResource)
    }

    private fun <T> checkIfActivityIsVisible(activityClass: Class<T>) {
        intended(hasComponent(activityClass.name))
    }

    private fun registerIdlingResource(idlingResource: IdlingResource) {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    private fun unregisterIdlingResource(idlingResource: IdlingResource) {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}