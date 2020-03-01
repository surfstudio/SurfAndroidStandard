package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.utils.ActivityAnimationSupplier

abstract class ActivityNavigator : ActivityNavigatorInterface {

    abstract val activity: AppCompatActivity

    protected open val animationSupplier = ActivityAnimationSupplier()

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
}