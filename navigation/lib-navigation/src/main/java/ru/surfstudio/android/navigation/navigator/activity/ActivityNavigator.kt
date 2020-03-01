package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.utils.ActivityAnimationSupplier

open class ActivityNavigator(open val activity: AppCompatActivity) : ActivityNavigatorInterface {

    protected open val animationSupplier = ActivityAnimationSupplier()

    override fun start(route: ActivityRoute, animations: Animations, optionsCompat: Bundle?) {
        val optionsWithAnimations =
                animationSupplier.supplyWithAnimations(activity, optionsCompat, animations)
        activity.startActivity(route.createIntent(activity), optionsWithAnimations)
    }

    override fun replace(route: ActivityRoute, animations: Animations, optionsCompat: Bundle?) {
        finish()
        start(route, animations, optionsCompat)
    }

    override fun finish() {
        activity.finish()
    }

    override fun finishAffinity() {
        activity.finishAffinity()
    }
}