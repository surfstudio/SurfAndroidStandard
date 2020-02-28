package ru.surfstudio.android.location.sample.mock

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Пример испоьзования кастомного TestRunner,
 * который переопределяет класс приложения
 * и использует замоканный LocationService
 */
@Suppress("unused")
class LocationSampleTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
            cl: ClassLoader?,
            className: String?,
            context: Context?
    ): Application {
        return super.newApplication(cl, TestCustomApp::class.java.name, context)
    }
}