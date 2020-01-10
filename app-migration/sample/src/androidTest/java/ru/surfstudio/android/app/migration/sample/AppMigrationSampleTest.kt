package ru.surfstudio.android.app.migration.sample

import org.junit.Test
import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.SplashActivityView
import ru.surfstudio.android.app.migration.sample.ui.screen.splash.TRANSITION_DELAY_MS
import ru.surfstudio.android.sample.common.test.ElapsedTimeIdlingResource
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.IdlingUtils.registerIdlingResource
import ru.surfstudio.android.sample.common.test.utils.IdlingUtils.unregisterIdlingResource

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