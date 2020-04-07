package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.close
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.toObservable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class KittiesMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val kittiesStorage: KittiesStorage,
        private val kittiesStateHolder: KittiesStateHolder
) : BaseMiddleware<KittiesEvent>(baseMiddlewareDependency) {

    private val state: KittiesState get() = kittiesStateHolder.value

    override fun transform(eventStream: Observable<KittiesEvent>): Observable<out KittiesEvent> {
        return transformations(eventStream) {
            addAll(
                    onCreate() eventMap { loadAllData() },
                    BackClicked::class mapTo { closeScreen() },

                    TopKitten.UpdateClicked::class mapTo { TopKitten.Load },
                    TopKitten.Load::class eventMapTo { onTopKittenLoad() },

                    NewKittiesCount.UpdateClicked::class mapTo { NewKittiesCount.Load },
                    NewKittiesCount.Load::class eventMapTo { onNewKittiesCountLoad() },

                    PopularKitties.AllClicked::class mapTo { openAllKittiesScreen() },
                    PopularKitties.UpdateClicked::class mapTo { onPopularKittiesUpdateClicked() },
                    PopularKitties.Load::class eventMapTo { onPopularKittiesLoad() },

                    Meow.Clicked::class mapTo { Meow.Send },
                    Meow.Send::class eventMapTo { onMeowSend() },
                    Meow.UpdateCount::class eventMapTo { onMeowUpdateCount() },
                    Meow.SendReq::class eventMapTo ::onMeowSendRequest,

                    Navigation::class decomposeTo navigationMiddleware
            )
        }
    }

    private fun onPopularKittiesUpdateClicked(): KittiesEvent {
        kittiesStorage.generateNewKittiesList()
        return PopularKitties.Load
    }

    private fun onTopKittenLoad(): Observable<out KittiesEvent> {
        return if (state.loadTopKittenRequestUi.isLoading) skip()
        else loadTopKitten()
    }

    private fun onNewKittiesCountLoad(): Observable<out KittiesEvent> {
        return if (state.loadNewKittiesCountRequestUi.isLoading) skip()
        else loadNewKittiesCount()
    }

    private fun onPopularKittiesLoad(): Observable<out KittiesEvent> {
        return if (state.loadPopularKittiesRequestUi.isLoading) skip()
        else loadPopularKitties()
    }

    private fun onMeowUpdateCount(): Observable<out KittiesEvent> {
        return if (state.isMeowButtonLoading) skip()
        else updateMeowCount()
    }

    private fun onMeowSend(): Observable<out KittiesEvent> {
        return if (state.isMeowButtonLoading) skip()
        else sendMeow()
    }

    private fun onMeowSendRequest(event: Meow.SendReq): Observable<out KittiesEvent> {
        return when (event.type) {
            is Request.Success -> Meow.UpdateCount.toObservable()
            else -> skip()
        }
    }

    private fun loadAllData(): Observable<out KittiesEvent> {
        val events = listOf(
                TopKitten.Load,
                NewKittiesCount.Load,
                PopularKitties.Load,
                Meow.UpdateCount
        )
        return Observable.fromIterable(events)
    }

    private fun loadTopKitten(): Observable<out KittiesEvent> {
        return fakeRequest(800L, kittiesStorage.getKittenOfTheWeek())
                .asRequestEvent(TopKitten.Req())
    }

    private fun loadNewKittiesCount(): Observable<out KittiesEvent> {
        return fakeRequest(1200L, kittiesStorage.getKittiesCount())
                .asRequestEvent(NewKittiesCount.Req())
    }

    private fun loadPopularKitties(): Observable<out KittiesEvent> {
        return fakeRequest(1600L, kittiesStorage.getPopularKitties())
                .asRequestEvent(PopularKitties.Req())
    }

    private fun updateMeowCount(): Observable<out KittiesEvent> {
        return fakeRequest(500L, kittiesStorage.getMeowsCount())
                .asRequestEvent(Meow.UpdateCountReq())
    }

    private fun sendMeow(): Observable<out KittiesEvent> {
        return fakeRequest(500L, kittiesStorage.sendMeow())
                .asRequestEvent(Meow.SendReq())
    }

    private fun openAllKittiesScreen(): KittiesEvent {
        return Navigation()//.open() // TODO add route
    }

    private fun closeScreen(): KittiesEvent {
        return Navigation().close()
    }

    private fun <T : Any> fakeRequest(delayMs: Long, result: T): Observable<T> {
        return Observable.just(result).delay(delayMs, TimeUnit.MILLISECONDS)
    }
}