package ru.surfstudio.android.custom_scope_sample.ui.base

import android.app.Activity
import ru.surfstudio.android.custom_scope_sample.app.AppInjector
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.DaggerLoginScreenComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginScreenComponent

/**
 * Хранилище компонента со скоупом @PerLogin
 */
object LoginScopeStorage {

    private val activityList: MutableList<Class<out Activity>> = mutableListOf()

    var loginScreenComponent: LoginScreenComponent? = null


    fun getLoginComponent(): LoginScreenComponent? {
        if (loginScreenComponent == null) {
            loginScreenComponent = DaggerLoginScreenComponent.builder()
                    .appComponent(AppInjector.appComponent)
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