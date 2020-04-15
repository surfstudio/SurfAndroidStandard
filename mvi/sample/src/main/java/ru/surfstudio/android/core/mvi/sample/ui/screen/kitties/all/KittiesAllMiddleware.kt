package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.close
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesStorage
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all.KittiesAllEvent.*
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class KittiesAllMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val kittiesStorage: KittiesStorage,
        private val kittiesAllStateHolder: KittiesAllStateHolder
) : BaseMiddleware<KittiesAllEvent>(baseMiddlewareDependency) {

    private val state get() = kittiesAllStateHolder.value

    override fun transform(eventStream: Observable<KittiesAllEvent>): Observable<out KittiesAllEvent> {
        return transformations(eventStream) {
            addAll(
                    Navigation::class decomposeTo navigationMiddleware,
                    onCreate() map { reloadKitties() },

                    // Ui events transformation:
                    Input.BackClicked::class mapTo { closeScreen() },
                    Input.SwrReload::class mapTo { reloadKitties(isSwr = true) },
                    Input.LoadNext::class mapTo { loadNextPageKitties() },

                    // Data events transformation:
                    Data.LoadKitties::class streamMapTo ::onLoadKitties
            )
        }
    }

    private fun reloadKitties(isSwr: Boolean = false): KittiesAllEvent {
        return Data.LoadKitties(offset = 0, isSwr = isSwr)
    }

    private fun loadNextPageKitties(): KittiesAllEvent {
        val nextOffset = state.kittiesRequestUi.data?.list?.nextOffset ?: 0
        return Data.LoadKitties(nextOffset, isSwr = false)
    }

    private fun onLoadKitties(observable: Observable<Data.LoadKitties>): Observable<out KittiesAllEvent> {
        return observable.switchMap { event ->
            fakeRequest(1500L, kittiesStorage.getPaginatedKitties(event.offset))
                    .asRequestEvent { LoadKittiesRequestEvent(it, isSwr = event.isSwr) }
        }
    }

    private fun closeScreen(): KittiesAllEvent {
        return Navigation().close()
    }

    private fun <T> fakeRequest(delayMs: Long, result: T): Observable<T> {
        return Observable.just(result).delay(delayMs, TimeUnit.MILLISECONDS)
    }
}