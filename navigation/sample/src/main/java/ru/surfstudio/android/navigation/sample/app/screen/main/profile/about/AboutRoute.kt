package ru.surfstudio.android.navigation.sample.app.screen.main.profile.about

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class AboutRoute : ActivityRoute(), ResultRoute<String> {

    override fun getScreenClass(): Class<out AppCompatActivity>? = AboutActivity::class.java
}