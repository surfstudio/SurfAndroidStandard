package ru.surfstudio.android.navigation.sample_standard.screen.splash

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.sample_standard.R

class SplashActivityView : BaseRxActivityView() {

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_main

    override fun getScreenName(): String = "Splash"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        Log.d("1111", "$intent")
    }
}