package ru.surfstudio.android.navigation.sample_standard.screen.splash

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class SplashRoute : ActivityRoute() {
    override fun getScreenClass(): Class<out AppCompatActivity> = SplashActivityView::class.java
}