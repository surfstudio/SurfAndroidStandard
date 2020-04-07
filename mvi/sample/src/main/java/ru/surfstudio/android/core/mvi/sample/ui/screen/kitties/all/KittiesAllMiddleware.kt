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

    private val state: KittiesAllState get() = kittiesAllStateHolder.value
    private val nextPaginationOffset: Int get() = state.kittiesRequestUi.data?.list?.nextOffset ?: 0

    override fun transform(eventStream: Observable<KittiesAllEvent>): Observable<out KittiesAllEvent> {
        return transformations(eventStream) {
            addAll(
                    onCreate() map { Content.LoadNext },
                    BackClicked::class mapTo { closeScreen() },

                    Content.LoadNext::class mapTo { Content.Load(nextPaginationOffset, false) },
                    Content.LoadSwr::class mapTo { Content.Load(0, true) },
                    Content.Load::class streamMapTo ::onLoadContent,

                    Navigation::class decomposeTo navigationMiddleware
            )
        }
    }

    private fun onLoadContent(observable: Observable<Content.Load>): Observable<out KittiesAllEvent> {
        return observable.switchMap { event ->
            fakeRequest(1500L, kittiesStorage.getPaginatedKitties(event.offset))
                    .asRequestEvent(Content.Req(isSwr = event.isSwr))
        }
    }

    private fun closeScreen(): KittiesAllEvent {
        return Navigation().close()
    }

    private fun <T> fakeRequest(delayMs: Long, result: T): Observable<T> {
        return Observable.just(result).delay(delayMs, TimeUnit.MILLISECONDS)
    }
}