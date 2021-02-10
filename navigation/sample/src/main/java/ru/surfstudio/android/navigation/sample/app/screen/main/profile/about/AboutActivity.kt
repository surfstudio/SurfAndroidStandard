package ru.surfstudio.android.navigation.sample.app.screen.main.profile.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import ru.surfstudio.android.navigation.sample.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        title = "About App Activity"

        close_btn.setOnClickListener {
            val appName = application.packageName

            val targetRoute = AboutRoute()
            // TODO вернуть результат
        }
    }
}