package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.close
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitty
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class KittiesMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<KittiesEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<KittiesEvent>): Observable<out KittiesEvent> {
        return transformations(eventStream) {
            addAll(
                    Input.BackClicked::class mapTo { closeActivity() },
                    Input.LoadTopKittyNameClicked::class mapTo { Data.LoadTopKittyName },
                    Input.LoadKittiesCountClicked::class mapTo { Data.LoadKittiesCount },
                    Input.LoadKittiesListClicked::class mapTo { Data.LoadKittiesList },
                    Input.SendMeowClicked::class mapTo { Data.SendMeow },

                    Data.LoadTopKittyName::class streamMapTo ::onLoadTopKittyName,
                    Data.LoadKittiesCount::class streamMapTo ::onLoadKittiesCount,
                    Data.LoadKittiesList::class streamMapTo ::onLoadKittiesList,
                    Data.SendMeow::class streamMapTo ::onSendMeow
            )
        }
    }

    private fun onLoadTopKittyName(observable: Observable<Data.LoadTopKittyName>): Observable<out KittiesEvent> {
        return observable.switchMap { loadTopKittyName() }
    }

    private fun onLoadKittiesCount(observable: Observable<Data.LoadKittiesCount>): Observable<out KittiesEvent> {
        return observable.switchMap { loadKittiesCount() }
    }

    private fun onLoadKittiesList(observable: Observable<Data.LoadKittiesList>): Observable<out KittiesEvent> {
        return observable.switchMap { loadKittiesList() }
    }

    private fun onSendMeow(observable: Observable<Data.SendMeow>): Observable<out KittiesEvent> {
        return observable.switchMap { sendMeow() }
    }

    private fun loadTopKittyName(): Observable<out KittiesEvent> {
        return delayedObservable(800L, "meow")
                .asRequestEvent(LoadTopKittyNameRequestEvent())
    }

    private fun loadKittiesCount(): Observable<out KittiesEvent> {
        return delayedObservable(500L, 2)
                .asRequestEvent(LoadKittiesCountRequestEvent())
    }

    private fun loadKittiesList(): Observable<out KittiesEvent> {
        return delayedObservable(1500L, emptyList<Kitty>())
                .asRequestEvent(LoadKittiesListRequestEvent())
    }

    private fun sendMeow(): Observable<out KittiesEvent> {
        return delayedObservable(500L, Unit)
                .asRequestEvent(SendMeowRequestEvent())
    }

    private fun closeActivity(): KittiesEvent {
        return Navigation().close()
    }

    private fun <T : Any> delayedObservable(delayMs: Long, content: T): Observable<T> {
        return Observable.just(content).delay(delayMs, TimeUnit.MILLISECONDS)
    }
}