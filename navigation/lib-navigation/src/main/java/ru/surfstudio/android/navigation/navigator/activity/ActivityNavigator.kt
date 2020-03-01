package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

open class ActivityNavigator(open val activity: AppCompatActivity) : ActivityNavigatorInterface {

    override fun start(route: ActivityRoute, animations: Animations, optionsCompat: Bundle?) {
        activity.startActivity(route.createIntent(activity), optionsCompat)
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

    protected open fun Bundle?.supplyWithAnimations() = this?.apply {

    }
}