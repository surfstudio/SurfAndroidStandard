package ru.surfstudio.android.navigation.observer.navigator.activity.deprecated

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorFactory

/**
 * Factory to create [ActivityNavigatorWithResultImpl]
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith(expression = "navigator.activity.ActivityNavigatorWithResultFactory")
)
open class ActivityNavigatorWithResultFactory : ActivityNavigatorFactory() {

    override fun create(
            parentActivity: AppCompatActivity
    ): ActivityNavigatorWithResult {
        return ActivityNavigatorWithResultImpl(parentActivity)
    }
}
