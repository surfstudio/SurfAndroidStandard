package ru.surfstudio.android.navigation.navigator.activity

import androidx.appcompat.app.AppCompatActivity

/**
 * Factory to create [ActivityNavigator]
 */
open class ActivityNavigatorFactory {

    open fun create(
            parentActivity: AppCompatActivity
    ): ActivityNavigatorInterface {
        return ActivityNavigator(parentActivity)
    }
}