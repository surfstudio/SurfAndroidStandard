package ru.surfstudio.standard

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.standard.common.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.standard.common.utils.ActivityUtils.launchActivity
import ru.surfstudio.standard.common.utils.IdlingUtils.registerIdlingResource
import ru.surfstudio.standard.common.utils.IdlingUtils.unregisterIdlingResource
import ru.surfstudio.standard.f_main.MainActivityView
import ru.surfstudio.standard.f_splash.SplashActivityView
import ru.surfstudio.standard.f_splash.TRANSITION_DELAY_MS
import ru.surfstudio.standard.common.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class TemplateTest {

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