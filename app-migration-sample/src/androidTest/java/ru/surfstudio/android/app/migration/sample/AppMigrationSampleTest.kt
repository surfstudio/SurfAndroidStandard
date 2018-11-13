package ru.surfstudio.android.app.migration.sample

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.SplashActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.TRANSITION_DELAY_MS
import ru.surfstudio.android.sample.common.test.ElapsedTimeIdlingResource
import ru.surfstudio.android.sample.common.test.utils.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppMigrationSampleTest {

    @Before
    fun setUp() {
        Intents.init()
        launchActivity(SplashActivityView::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testApplicationLaunch() {
        checkIfActivityIsVisible(SplashActivityView::class.java)

        // Ожидаем, пока показывается splash-экран
        val idlingResource = ElapsedTimeIdlingResource(TRANSITION_DELAY_MS)
        registerIdlingResource(idlingResource)

        checkIfActivityIsVisible(MainActivityView::class.java)
        unregisterIdlingResource(idlingResource)
    }
}