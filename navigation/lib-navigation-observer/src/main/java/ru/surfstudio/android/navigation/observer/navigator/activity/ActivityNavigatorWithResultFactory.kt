package ru.surfstudio.android.navigation.observer.navigator.activity

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorFactory
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.storage.RouteStorage

/**
 * Factory to create [ActivityNavigatorWithResultImpl]
 */
open class ActivityNavigatorWithResultFactory(
    private val screenResultEmitter: ScreenResultEmitter,
    private val routeStorage: RouteStorage
) : ActivityNavigatorFactory() {

    override fun create(
        parentActivity: AppCompatActivity
    ): ActivityNavigatorWithResult {
        return ActivityNavigatorWithResultImpl(parentActivity, screenResultEmitter, routeStorage)
    }
}