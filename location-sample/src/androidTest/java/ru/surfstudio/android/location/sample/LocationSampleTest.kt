package ru.surfstudio.android.location.sample

import android.app.Activity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample.DefaultLocationInteractorActivityView
import ru.surfstudio.android.location.sample.ui.screen.location_service_sample.LocationServiceActivityView
import ru.surfstudio.android.location.sample.ui.screen.start.MainActivity
import ru.surfstudio.android.location_sample.R
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationSampleTest {

    private val defaultLocationInteractorOptions = intArrayOf(
            R.id.btn_activity_default_location_interactor_check_location_availability,
            R.id.btn_activity_default_location_interactor_resolve_location_availability,
            R.id.btn_activity_default_location_interactor_show_last_known_location,
            R.id.btn_activity_default_location_interactor_show_current_location
    )

    private val locationServiceOptions = intArrayOf(
            R.id.btn_activity_location_service_check_location_availability,
            R.id.btn_activity_location_service_resolve_location_availability,
            R.id.btn_activity_location_service_show_last_known_location,
            R.id.btn_activity_location_service_subscribe_to_location_updates,
            R.id.btn_activity_location_service_unsubscribe_from_location_updates
    )

    @Before
    fun setUp() {
        Intents.init()
        launchActivity(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testLocationSample() {
        clickAndCheckActivity(
                R.id.btn_default_location_interactor_sample,
                DefaultLocationInteractorActivityView::class.java,
                defaultLocationInteractorOptions
        )
        clickAndCheckActivity(
                R.id.btn_location_service_sample,
                LocationServiceActivityView::class.java,
                locationServiceOptions
        )
    }

    private fun <T : Activity> clickAndCheckActivity(
            @IdRes buttonResId: Int,
            activityClass: Class<T>,
            buttons: IntArray
    ) {
        performClick(buttonResId)
        checkIfActivityIsVisible(activityClass)
        performClick(*buttons)
        Espresso.pressBack()
    }
}