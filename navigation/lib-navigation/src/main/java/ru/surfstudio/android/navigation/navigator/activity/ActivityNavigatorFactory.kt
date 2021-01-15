package ru.surfstudio.android.navigation.navigator.activity

import androidx.appcompat.app.AppCompatActivity

open class ActivityNavigatorFactory {

    open fun create(
            parentActivity: AppCompatActivity
    ): ActivityNavigatorInterface {
        return ActivityNavigator(parentActivity)
    }
}