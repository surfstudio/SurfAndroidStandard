package ru.surfstudio.android.navigation.sample.app.screen.main.profile.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.provider.id.IdentifiableScreen
import ru.surfstudio.android.navigation.sample.R

class AboutActivity : AppCompatActivity(), IdentifiableScreen {

    override val screenId: String = "AboutApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        title = "About App Activity"
    }
}