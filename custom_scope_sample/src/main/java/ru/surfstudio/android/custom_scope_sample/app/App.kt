package ru.surfstudio.android.custom_scope_sample.app

import ru.surfstudio.android.core.app.CoreApp

/**
 * Класс приложения
 */
class App : CoreApp() {

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        AppConfigurator.initInjector(this)
    }
}