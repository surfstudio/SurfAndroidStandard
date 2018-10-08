package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_location_service.*
import ru.surfstudio.android.location.sample.ui.screen.common.BaseSampleActivity
import ru.surfstudio.android.location_sample.R
import javax.inject.Inject

/**
 * Экран примера использования [LocationService]
 */
class LocationServiceActivityView : BaseSampleActivity() {

    @Inject
    lateinit var presenter: LocationServicePresenter

    override fun createConfigurator() = LocationServiceScreenConfigurator(intent)

    override fun getScreenName() = "LocationServiceActivityView"

    override fun getContentView() = R.layout.activity_location_service

    override fun getPresenters() = arrayOf(presenter)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        title = "LocationService sample"

        btn_activity_location_service_unsubscribe_from_location_updates.isEnabled = false

        cb_no_location_reprmission_resolution.setOnClickListener {
            if (cb_no_location_reprmission_resolution.isChecked) {
                presenter.addNoLocationPermissionResolution()
            } else {
                presenter.removeNoLocationPermissionResolution()
            }
        }

        cb_play_services_are_not_available_resolution.setOnClickListener {
            if (cb_play_services_are_not_available_resolution.isChecked) {
                presenter.addPlayServicesAreNotAvailableResolution()
            } else {
                presenter.removePlayServicesAreNotAvailableResolution()
            }
        }

        cb_resolvable_api_exception_resolution.setOnClickListener {
            if (cb_resolvable_api_exception_resolution.isChecked) {
                presenter.addResolvableApiExceptionResolution()
            } else {
                presenter.removeResolvableApiExceptionResolution()
            }
        }

        btn_activity_location_service_check_location_availability.setOnClickListener {
            presenter.checkLocationAvailability()
        }
        btn_activity_location_service_resolve_location_availability.setOnClickListener {
            presenter.resolveLocationAvailability()
        }
        btn_activity_location_service_show_last_known_location.setOnClickListener {
            presenter.getLastKnownLocation()
        }
        btn_activity_location_service_subscribe_to_location_updates.setOnClickListener {
            presenter.subscribeToLocationUpdates()
            setLocationUpdatesModeEnabled(true)
        }
        btn_activity_location_service_unsubscribe_from_location_updates.setOnClickListener {
            presenter.unsubscribeFromLocationUpdates()
            setLocationUpdatesModeEnabled(false)
        }
    }

    private fun setLocationUpdatesModeEnabled(isEnabled: Boolean) {
        btn_activity_location_service_unsubscribe_from_location_updates.isEnabled = isEnabled
        cb_no_location_reprmission_resolution.isEnabled = !isEnabled
        cb_play_services_are_not_available_resolution.isEnabled = !isEnabled
        cb_resolvable_api_exception_resolution.isEnabled = !isEnabled
        btn_activity_location_service_check_location_availability.isEnabled = !isEnabled
        btn_activity_location_service_resolve_location_availability.isEnabled = !isEnabled
        btn_activity_location_service_show_last_known_location.isEnabled = !isEnabled
        btn_activity_location_service_subscribe_to_location_updates.isEnabled = !isEnabled
    }
}