package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_default_location_interactor.*
import ru.surfstudio.android.location.sample.ui.screen.common.BaseSampleActivity
import ru.surfstudio.android.location_sample.R
import javax.inject.Inject

/**
 * Экран примера использования [DefaultLocationInteractor]
 */
class DefaultLocationInteractorActivityView : BaseSampleActivity() {

    @Inject
    lateinit var presenter: DefaultLocationInteractorPresenter

    override fun createConfigurator() = DefaultLocationInteractorScreenConfigurator(intent)

    override fun getScreenName() = "DefaultLocationInteractorActivityView"

    override fun getContentView() = R.layout.activity_default_location_interactor

    override fun getPresenters() = arrayOf(presenter)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        title = "DefaultLocationInteractor sample"

        btn_activity_default_location_interactor_check_location_availability.setOnClickListener {
            presenter.checkLocationAvailability()
        }
        btn_activity_default_location_interactor_resolve_location_availability.setOnClickListener {
            presenter.resolveLocationAvailability()
        }
        btn_activity_default_location_interactor_show_last_known_location.setOnClickListener {
            presenter.getLastKnownLocation()
        }
        btn_activity_default_location_interactor_show_current_location.setOnClickListener {
            presenter.getCurrentLocation()
        }
    }
}