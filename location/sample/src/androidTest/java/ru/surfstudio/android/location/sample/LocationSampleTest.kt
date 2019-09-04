package ru.surfstudio.android.location.sample

import android.Manifest
import android.app.Activity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample.DefaultLocationInteractorActivityView
import ru.surfstudio.android.location.sample.ui.screen.location_service_sample.LocationServiceActivityView
import ru.surfstudio.android.location.sample.ui.screen.start.MainActivity
import ru.surfstudio.android.location_sample.R
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.TextUtils
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick

@RunWith(AndroidJUnit4::class)
class LocationSampleTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val defaultLocationServiceOptions = intArrayOf(
            R.id.btn_activity_default_location_interactor_check_location_availability,
            R.id.btn_activity_default_location_interactor_show_last_known_location,
            R.id.btn_activity_default_location_interactor_show_current_location
    )

    private val locationServiceOptions = intArrayOf(
            R.id.btn_activity_location_service_check_location_availability,
            R.id.btn_activity_location_service_resolve_location_availability,
            R.id.btn_activity_location_service_show_last_known_location,
            R.id.btn_activity_location_service_subscribe_to_location_updates,
            R.id.btn_activity_location_service_unsubscribe_from_location_updates
    )

    private lateinit var uiDevice: UiDevice
    private val timeout = 4000L

    @Before
    fun setUp() {
        setGpsEnabled(true)
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Intents.init()
        ActivityUtils.launchActivity(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        setGpsEnabled(false)
        Intents.release()
    }

    @Test
    fun testLocationSample() {
        // Test first screen
        performClick(R.id.btn_default_location_interactor_sample)
        checkIfActivityIsVisible(DefaultLocationInteractorActivityView::class.java)
        performClick(R.id.btn_activity_default_location_interactor_resolve_location_availability)
        uiDevice.wait(
                Until.findObject(By.text(TextUtils.getString(R.string.accept_location_settings_btn_text))),
                timeout
        )
        acceptLocationSettingsDialog()
        performClick(*defaultLocationServiceOptions)
        Espresso.pressBack()

        // Test second screen
        clickAndCheckActivity(
                R.id.btn_location_service_sample,
                LocationServiceActivityView::class.java,
                locationServiceOptions
        )
    }

    private fun <T : Activity> clickAndCheckActivity(
            @IdRes buttonResId: Int,
            activityClass: Class<T>,
            buttons: IntArray
    ) {
        performClick(buttonResId)
        checkIfActivityIsVisible(activityClass)
        performClick({ acceptLocationSettingsDialog() }, *buttons)
        Espresso.pressBack()
    }

    private fun acceptLocationSettingsDialog() {
        uiDevice.apply {
            findObject(UiSelector().text(
                    TextUtils.getString(R.string.accept_location_settings_btn_text))
            ).apply {
                if (exists() && isEnabled) {
                    click()
                }
            }
        }
    }

    private fun setGpsEnabled(isEnabled: Boolean) {
        getInstrumentation().uiAutomation.executeShellCommand(
                "settings put secure location_providers_allowed " +
                        "${if (isEnabled) '+' else '-'}gps")
    }
}