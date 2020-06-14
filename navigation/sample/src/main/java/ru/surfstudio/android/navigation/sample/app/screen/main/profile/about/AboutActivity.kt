package ru.surfstudio.android.navigation.sample.app.screen.main.profile.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.ProfileTabRoute

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        title = "About App Activity"

        close_btn.setOnClickListener {
            val appName = application.packageName

            val sourceRoute = ProfileTabRoute()
            val targetRoute = AboutRoute()

            App.navigator.execute(listOf(
                    Finish(),
                    EmitScreenResult(sourceRoute, targetRoute, appName)
            ))
        }
    }
}