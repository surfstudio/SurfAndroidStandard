package ru.surfstudio.android.navigation.observer.deprecated.navigator.activity

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorFactory
import ru.surfstudio.android.navigation.observer.deprecated.navigator.activity.ActivityNavigatorWithResult
import ru.surfstudio.android.navigation.observer.deprecated.navigator.activity.ActivityNavigatorWithResultImpl

/**
 * Factory to create [ActivityNavigatorWithResultImpl]
 */
@Deprecated("Prefer using new implementation")
open class ActivityNavigatorWithResultFactory : ActivityNavigatorFactory() {

    override fun create(
            parentActivity: AppCompatActivity
    ): ActivityNavigatorWithResult {
        return ActivityNavigatorWithResultImpl(parentActivity)
    }
}