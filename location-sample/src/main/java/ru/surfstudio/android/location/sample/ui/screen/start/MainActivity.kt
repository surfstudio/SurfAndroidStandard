package ru.surfstudio.android.location.sample.ui.screen.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample.DefaultLocationInteractorActivityRoute
import ru.surfstudio.android.location.sample.ui.screen.location_service_sample.LocationServiceActivityRoute
import ru.surfstudio.android.location_sample.R

/**
 * Главный экран
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btn_default_location_interactor_sample.setOnClickListener {
            startActivity(DefaultLocationInteractorActivityRoute().prepareIntent(this))
        }
        btn_location_service_sample.setOnClickListener {
            startActivity(LocationServiceActivityRoute().prepareIntent(this))
        }
    }
}