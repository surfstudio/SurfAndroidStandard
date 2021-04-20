package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.animation.utils.ActivityAnimationSupplier

open class ActivityNavigator(val activity: AppCompatActivity) : ActivityNavigatorInterface {

    protected open val animationSupplier = ActivityAnimationSupplier()

    override fun start(routes: List<ActivityRoute>, animations: Animations, activityOptions: Bundle?) {
        val intents = Array(routes.size) { index: Int ->
            val route = routes[index]
            route.createIntent(activity)
        }
        val optionsWithAnimations: Bundle? =
                animationSupplier.supplyWithAnimations(activity, activityOptions, animations)
        activity.startActivities(intents, optionsWithAnimations)
    }

    override fun start(route: ActivityRoute, animations: Animations, activityOptions: Bundle?) {
        val optionsWithAnimations =
                animationSupplier.supplyWithAnimations(activity, activityOptions, animations)
        activity.startActivity(route.createIntent(activity), optionsWithAnimations)
    }

    override fun replace(route: ActivityRoute, animations: Animations, activityOptions: Bundle?) {
        finish()
        start(route, animations, activityOptions)
    }

    override fun finish() {
        activity.finish()
    }

    override fun finishAffinity() {
        activity.finishAffinity()
    }

    override fun canBeStarted(route: ActivityRoute): Boolean {
        val intent = route.createIntent(activity)
        val hasActivityComponent = intent.resolveActivity(activity.packageManager) != null
        return hasActivityComponent
    }
}