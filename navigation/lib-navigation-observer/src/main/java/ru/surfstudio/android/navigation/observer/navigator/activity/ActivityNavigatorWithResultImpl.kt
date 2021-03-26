package ru.surfstudio.android.navigation.observer.navigator.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.observer.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.observer.route.PermissionRequestRoute
import java.io.Serializable

/**
 *  The activity navigator which supports start for result.
 */
open class ActivityNavigatorWithResultImpl(
    activity: AppCompatActivity
) : ActivityNavigator(activity), ActivityNavigatorWithResult {

    private val launcher: ActivityResultLauncher<Intent>
    private var resultRegistration: ActivityResultRegistration<Serializable>? = null
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
    private var permissionResultCallback: ((Boolean) -> Unit)? = null

    init {
        launcher =
            (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data =
                    resultRegistration?.route?.parseResultIntent(result.resultCode, result.data)
                data?.let { resultRegistration?.callback?.invoke(data) }
                resultRegistration = null
            }

        permissionLauncher =
            (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                permissionResultCallback?.invoke(result.values.all { it })
                permissionResultCallback = null
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Serializable> callbackResult(
        route: ActivityWithResultRoute<T>,
        callback: ScreenResultListener<T>
    ) {
        resultRegistration = ActivityResultRegistration(
            route,
            callback
        ) as ActivityResultRegistration<Serializable>
    }

    override fun startForResult(
        route: ActivityWithResultRoute<*>,
        animations: Animations,
        activityOptions: Bundle?
    ) {
        check(isCallbackRegistered()) {
            "You must register callback by method ActivityNavigatorWithResult#callbackResult before starting activity"
        }
        val intent: Intent = route.createIntent(activity)
        // todo ActivityAnimationSupplier must return ActivityOptionsCompat to launch activity with animations
        launcher.launch(intent)
    }

    override fun requestPermission(
        route: PermissionRequestRoute,
        resultCallback: (Boolean) -> Unit
    ) {
        permissionResultCallback = resultCallback
        permissionLauncher.launch(route.permissions)
    }

    private fun isCallbackRegistered(): Boolean {
        return resultRegistration != null
    }

    private class ActivityResultRegistration<T : Serializable>(
        val route: ActivityWithResultRoute<T>,
        val callback: ScreenResultListener<T>
    )
}
