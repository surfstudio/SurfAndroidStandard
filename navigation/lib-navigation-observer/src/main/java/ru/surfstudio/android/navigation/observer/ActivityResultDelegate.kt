package ru.surfstudio.android.navigation.observer

import android.app.Activity
import android.content.Intent
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.route.result.ActivityResultData
import ru.surfstudio.android.navigation.route.result.SystemActivityResultRoute
import java.io.Serializable

/**
 * Delegate for activity result
 */
class ActivityResultDelegate(
        private val screenResultEmitter: ScreenResultEmitter,
        private val appCommandExecutor: AppCommandExecutor
) {

    @Suppress("UNCHECKED_CAST")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        screenResultEmitter.getObservedRoutes().forEach { route ->
            val activityResultRoute = route as? SystemActivityResultRoute<ActivityResultData<Serializable>>
            if (activityResultRoute?.getRequestCode() == requestCode) {
                emitActivityResult(route, resultCode, data)
            }
        }
    }

    private fun emitActivityResult(route: SystemActivityResultRoute<ActivityResultData<Serializable>>, resultCode: Int, data: Intent?) {
        val result = ActivityResultData(
                resultCode == Activity.RESULT_OK,
                route.parseResultIntent(data)
        )
        appCommandExecutor.execute(EmitScreenResult(route, result))
    }
}