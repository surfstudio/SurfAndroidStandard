package ru.surfstudio.android.core.mvp.binding.test.navigation

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.FinishAffinity
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.FinishCurrent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.RouteEvent.*
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.BaseTestNavigator
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.NewIntentRoute
import ru.surfstudio.android.core.ui.navigation.event.result.CrossFeatureSupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallState
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Тестовая реализация [ActivityNavigator]
 */
class TestActivityNavigator : BaseTestNavigator<TestActivityNavigationEvent>(), ActivityNavigator {

    private val activityResultMap = mutableMapOf<Class<out SupportOnActivityResultRoute<*>>, PublishSubject<ScreenResult<Serializable?>>>()
    private val newIntentMap = mutableMapOf<Class<out NewIntentRoute>, PublishSubject<NewIntentRoute>>()

    override fun reset() {
        super.reset()
        activityResultMap.clear()
        newIntentMap.clear()
    }

    fun <T : Serializable> emitActivityResult(routeClass: KClass<out SupportOnActivityResultRoute<T>>, result: T? = null, success: Boolean = true) {
        val subject = activityResultMap[routeClass.java]
            ?: throw RuntimeException("${routeClass.java} isn't being observed")

        subject.onNext(ScreenResult(success, result))
    }

    fun <T : NewIntentRoute> emitNewIntent(route: T) {
        val subject = newIntentMap[route::class.java]
            ?: throw RuntimeException("${route::class.java} isn't being observed")

        subject.onNext(route)
    }

    override fun <T : Serializable> observeResult(routeClass: Class<out SupportOnActivityResultRoute<T>>): Observable<ScreenResult<T?>> {
        val subject = PublishSubject.create<ScreenResult<Serializable?>>()
        activityResultMap[routeClass] = subject
        @Suppress("UNCHECKED_CAST")
        return subject.map { ScreenResult(it.isSuccess, it.data as T?) }.hide()
    }

    override fun <T : Serializable> observeResult(route: SupportOnActivityResultRoute<T>): Observable<ScreenResult<T?>> {
        return observeResult(route::class.java)
    }

    override fun finishCurrent() {
        mutableEvents.add(FinishCurrent)
    }

    override fun finishAffinity() {
        mutableEvents.add(FinishAffinity)
    }

    override fun <T : Serializable> finishWithResult(route: ActivityWithResultRoute<T>, success: Boolean) {
        finishWithResult(route, null, success)
    }

    override fun <T : Serializable> finishWithResult(activeScreenRoute: SupportOnActivityResultRoute<T>, result: T?) {
        finishWithResult(activeScreenRoute, result, true)
    }

    override fun <T : Serializable> finishWithResult(currentScreenRoute: SupportOnActivityResultRoute<T>, result: T?, success: Boolean) {
        mutableEvents.add(FinishWithResult(currentScreenRoute, result, success))
    }

    override fun start(route: ActivityRoute): Boolean {
        mutableEvents.add(Start(route))
        return true
    }

    override fun start(route: ActivityCrossFeatureRoute): Observable<SplitFeatureInstallState> {
        mutableEvents.add(Start(route))
        return Observable.empty()
    }

    override fun startForResult(route: CrossFeatureSupportOnActivityResultRoute<*>): Observable<SplitFeatureInstallState> {
        mutableEvents.add(StartForResult(route))
        return Observable.empty()
    }

    override fun startForResult(route: SupportOnActivityResultRoute<*>): Boolean {
        mutableEvents.add(StartForResult(route))
        return true
    }

    override fun <T : NewIntentRoute> observeNewIntent(newIntentRouteClass: Class<T>): Observable<T> {
        val subject = PublishSubject.create<NewIntentRoute>()
        newIntentMap[newIntentRouteClass] = subject
        @Suppress("UNCHECKED_CAST")
        return subject.map { it as T }.hide()
    }

    override fun <T : NewIntentRoute> observeNewIntent(newIntentRoute: T): Observable<T> {
        @Suppress("UNCHECKED_CAST")
        return observeNewIntent(newIntentRoute::class.java) as Observable<T>
    }
}
