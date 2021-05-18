package ru.surfstudio.android.navigation.observer.navigator.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.observer.executor.ScreenResultDispatcher
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.observer.route.PermissionRequestRoute
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.observer.storage.RouteStorage
import java.io.Serializable

/**
 *  The activity navigator which supports start for result.
 */
open class ActivityNavigatorWithResultImpl(
    activity: AppCompatActivity,
    private val screenResultEmitter: ScreenResultEmitter,
    private val routeStorage: RouteStorage
) : ActivityNavigator(activity), ActivityNavigatorWithResult {

    private val screenResultDispatcher = ScreenResultDispatcher()
    private val activityLauncher: ActivityResultLauncher<Intent>
    private val permissionLauncher: ActivityResultLauncher<Array<String>>

    init {
        val componentActivity = activity as ComponentActivity
        activityLauncher = componentActivity
            .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleActivityResult(result)
            }
        permissionLauncher = componentActivity
            .registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                handlePermissionResult(result.values.all { it })
            }
    }

    override fun startForResult(
        route: ActivityWithResultRoute<*>,
        animations: Animations,
        activityOptions: Bundle?
    ) {
        routeStorage.save(route)
        val intent: Intent = route.createIntent(activity)
        // todo ActivityAnimationSupplier must return ActivityOptionsCompat to launch activity with animations
        activityLauncher.launch(intent)
    }

    override fun requestPermission(
        route: PermissionRequestRoute
    ) {
        permissionLauncher.launch(route.permissions)
    }

    private fun handleActivityResult(result: ActivityResult) {
        routeStorage.get<Serializable>()?.let {
            val data = it.parseResultIntent(result.resultCode, result.data)
            emitScreenResult(it, data)
            routeStorage.clear()
        }
    }

    private fun handlePermissionResult(result: Boolean) {
        emitScreenResult(PermissionRequestRoute(emptyArray()), result)
    }

    private fun <T : Serializable> emitScreenResult(route: ResultRoute<T>, data: T) {
        val emitResultCommand = EmitScreenResult(route, data)
        screenResultDispatcher.dispatch(screenResultEmitter, emitResultCommand)
    }
}
