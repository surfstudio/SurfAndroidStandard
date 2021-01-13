package ru.surfstudio.android.navigation.rx.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.route.result.ResultRoute
import ru.surfstudio.android.navigation.rx.ListenForScreenResultObservable
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