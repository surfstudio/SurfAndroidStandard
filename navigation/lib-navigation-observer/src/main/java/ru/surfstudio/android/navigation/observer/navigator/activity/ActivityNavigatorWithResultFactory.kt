package ru.surfstudio.android.navigation.observer.navigator.activity

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorFactory

/**
 * Factory to create [ActivityNavigatorWithResultImpl]
 */
open class ActivityNavigatorWithResultFactory : ActivityNavigatorFactory() {

    override fun create(
            parentActivity: AppCompatActivity
    ): ActivityNavigatorWithResult {
        return ActivityNavigatorWithResultImpl(parentActivity)
    }
}