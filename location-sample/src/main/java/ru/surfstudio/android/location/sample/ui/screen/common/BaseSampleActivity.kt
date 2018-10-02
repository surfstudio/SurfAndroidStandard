package ru.surfstudio.android.location.sample.ui.screen.common

import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isGone
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.location_sample.R

/**
 * Базовая активити для примеров использования модуля местоположения
 */
abstract class BaseSampleActivity : CoreActivityView() {

    private val containerLoading by lazy { findViewById<FrameLayout>(R.id.container_loading) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showLocation(location: Location) {
        showToast("Location: lat = ${location.latitude}, lon = ${location.longitude})")
    }

    fun showNoLocation() {
        showToast("No location")
    }

    fun showLocationIsAvailable() {
        showToast("Location is available")
    }

    fun showLocationIsNotAvailable(t: Throwable) {
        showToast(buildLocationIsNotAvailableStr(t))
    }

    fun showLoading() {
        containerLoading.isGone = false
    }

    fun hideLoading() {
        containerLoading.isGone = true
    }

    private fun buildLocationIsNotAvailableStr(t: Throwable): String {
        return buildString {
            append("Error:\n")

            if (t !is CompositeException) {
                append(t::class.java.simpleName)
                return@buildString
            }

            for (exception in t.exceptions) {
                append("\n - ")
                append(exception::class.java.simpleName)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}