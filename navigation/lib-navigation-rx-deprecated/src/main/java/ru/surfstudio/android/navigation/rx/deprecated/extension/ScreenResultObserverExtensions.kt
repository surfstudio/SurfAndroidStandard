package ru.surfstudio.android.navigation.rx.deprecated.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.observer.deprecated.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.deprecated.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.rx.deprecated.ListenForScreenResultObservable
import java.io.Serializable

/**
 * Observes screen all screen results emitted by screen associated with [targetRoute]
 */
fun <T : Serializable, R> ScreenResultObserver.observeScreenResult(
        targetRoute: R
): Observable<T> where R : BaseRoute<*>, R : ResultRoute<T> =
        ListenForScreenResultObservable(
                this,
                targetRoute
        )