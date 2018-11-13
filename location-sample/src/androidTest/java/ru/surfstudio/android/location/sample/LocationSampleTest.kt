package ru.surfstudio.android.location.sample

import android.app.Activity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample.DefaultLocationInteractorActivityView
import ru.surfstudio.android.location.sample.ui.screen.location_service_sample.LocationServiceActivityView
import ru.surfstudio.android.location.sample.ui.screen.start.MainActivity
import ru.surfstudio.android.location_sample.R
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

class LocationSampleTest : BaseSampleTest<MainActivity>(MainActivity::class.java) {

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