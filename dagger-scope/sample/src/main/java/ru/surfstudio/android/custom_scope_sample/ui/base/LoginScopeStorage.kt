package ru.surfstudio.android.custom_scope_sample.ui.base

import android.app.Activity
import ru.surfstudio.android.custom_scope_sample.app.AppConfigurator
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.DaggerLoginComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginComponent

/**
 * Хранилище компонента со скоупом @PerLogin
 */
object LoginScopeStorage {

    private val activityList: MutableList<Class<out Activity>> = mutableListOf()

    private var loginScreenComponent: LoginComponent? = null

    fun getLoginComponent(): LoginComponent? {
        if (loginScreenComponent == null) {
            loginScreenComponent = DaggerLoginComponent.builder()
                    .appComponent(AppConfigurator.appComponent)
                    .build()
        }

        return loginScreenComponent
    }

    fun clearComponent() {
        loginScreenComponent = null
    }

    fun addActivity(clazz: Class<out Activity>) {
        activityList.add(clazz)
    }

    fun removeActivity(clazz: Class<out Activity>) {
        activityList.remove(clazz)
        if (activityList.isEmpty()) clearComponent()
    }
}