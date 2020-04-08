package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.close
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.open
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.all.KittiesAllActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class KittiesMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val kittiesStorage: KittiesStorage,
        private val kittiesStateHolder: KittiesStateHolder
) : BaseMiddleware<KittiesEvent>(baseMiddlewareDependency) {

    private val state get() = kittiesStateHolder.value
    private val isTopKittenLoading get() = state.loadTopKittenRequestUi.isLoading
    private val isNewKittiesCountLoading get() = state.loadNewKittiesCountRequestUi.isLoading
    private val isPopularKittiesLoading get() = state.loadPopularKittiesRequestUi.isLoading
    private val isMeowLoading get() = state.isMeowButtonLoading

    override fun transform(eventStream: Observable<KittiesEvent>): Observable<out KittiesEvent> {
        return transformations(eventStream) {
            addAll(
                    onCreate() eventMap { loadAllData() },
                    BackClicked::class mapTo { closeScreen() },

                    TopKitten.UpdateClicked::class mapTo { TopKitten.Load },
                    TopKitten.Load::class.filter { !isTopKittenLoading }.eventMap { loadTopKitten() },

                    NewKittiesCount.UpdateClicked::class mapTo { NewKittiesCount.Load },
                    NewKittiesCount.Load::class.filter { !isNewKittiesCountLoading }.eventMap { loadNewKittiesCount() },

                    PopularKitties.AllClicked::class mapTo { openAllKittiesScreen() },
                    PopularKitties.UpdateClicked::class mapTo {
                        kittiesStorage.generateNewKittiesList()
                        PopularKitties.Load
                    },
                    PopularKitties.Load::class.filter { !isPopularKittiesLoading }.eventMap { loadPopularKitties() },

                    Meow.Clicked::class mapTo { Meow.Send },
                    Meow.Send::class.filter { !isMeowLoading }.eventMap { sendMeow() },
                    Meow.SendReq::class.filter { it.hasData }.map { Meow.UpdateCount },
                    Meow.UpdateCount::class.filter { !isMeowLoading }.eventMap { updateMeowCount() },

                    Navigation::class decomposeTo navigationMiddleware
            )
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
        return Navigation().open(KittiesAllActivityRoute())
    }

    private fun closeScreen(): KittiesEvent {
        return Navigation().close()
    }

    private fun <T : Any> fakeRequest(delayMs: Long, result: T): Observable<T> {
        return Observable.just(result).delay(delayMs, TimeUnit.MILLISECONDS)
    }
}