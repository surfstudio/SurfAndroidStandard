package ru.surfstudio.android.navigation.sample_standard.screen.main

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class MainRoute() : ActivityRoute() {
    override fun getScreenClass(): Class<out AppCompatActivity> = MainActivityView::class.java
}