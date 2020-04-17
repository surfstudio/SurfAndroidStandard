package ru.surfstudio.android.navigation.sample.app.screen.main.profile.about

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class AboutRoute : ActivityRoute() {

    override fun getScreenClass(): Class<out AppCompatActivity>? = AboutActivity::class.java
}