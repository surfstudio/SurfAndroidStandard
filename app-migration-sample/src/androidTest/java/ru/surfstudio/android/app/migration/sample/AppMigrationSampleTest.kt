package ru.surfstudio.android.app.migration.sample

import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.SplashActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.TRANSITION_DELAY_MS

class AppMigrationSampleTest : BaseSampleTest<SplashActivityView>(SplashActivityView::class.java) {

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