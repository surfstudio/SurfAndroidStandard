package ru.surfstudio.android.navigation.rx.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.rx.ListenForScreenResultObservable
import java.io.Serializable

fun <T : Serializable, R> ScreenResultObserver.observeScreenResult(
        targetRoute: R
): Observable<T> where R : BaseRoute<*>, R : ResultRoute<T> =
        ListenForScreenResultObservable(
                this,
                targetRoute
        )