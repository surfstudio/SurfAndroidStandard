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
import java.io.Serializable

open class ActivityNavigatorWithResult(
        activity: AppCompatActivity
) : ActivityNavigator(activity), ActivityNavigatorWithResultInterface {

    private val launcher: ActivityResultLauncher<Intent>
    private var resultRegistration: ActivityResultRegistration<Serializable>? = null

    init {
        launcher = (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = resultRegistration?.route?.parseResultIntent(result.resultCode, result.data)
            data?.let { resultRegistration?.callback?.invoke(data) }
            resultRegistration = null
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

    override fun startForResult(route: ActivityWithResultRoute<*>, animations: Animations, activityOptions: Bundle?) {
        check(isCallbackRegistered()) {
            "You must register callback by method ActivityNavigatorWithResult#callbackResult before starting activity"
        }
        val intent: Intent = route.createIntent(activity)
        //todo ActivityAnimationSupplier must return ActivityOptionsCompat to launch activity with animations
        launcher.launch(intent)
    }

    private fun isCallbackRegistered(): Boolean {
        return resultRegistration != null
    }

    private class ActivityResultRegistration<T : Serializable>(
            val route: ActivityWithResultRoute<T>,
            val callback: ScreenResultListener<T>
    )
}