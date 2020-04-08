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

    override fun transform(eventStream: Observable<KittiesEvent>): Observable<out KittiesEvent> {
        return transformations(eventStream) {
            addAll(
                    onCreate() eventMap { loadAllData() },
                    Navigation::class decomposeTo navigationMiddleware,

                    // Ui events transformation:
                    Input.BackClicked::class mapTo { closeScreen() },
                    Input.TopKittenUpdateClicked::class.mapTo { Data.LoadTopKitten },
                    Input.NewKittiesCountUpdateClicked::class mapTo { Data.LoadNewKittiesCount },
                    Input.PopularKittiesUpdateClicked::class mapTo { generateAndLoadNewPopularList() },
                    Input.PopularKittiesAllClicked::class mapTo { openAllKittiesScreen() },
                    Input.MeowClicked::class mapTo { Data.SendMeow },

                    // Data events transformation:
                    Data.LoadTopKitten::class.filter { !state.isTopKittenLoading }.eventMap { loadTopKitten() },
                    Data.LoadNewKittiesCount::class.filter { !state.isNewKittiesCountLoading }.eventMap { loadNewKittiesCount() },
                    Data.LoadPopularKittes::class.filter { !state.isPopularKittiesLoading }.eventMap { loadPopularKitties() },
                    Data.SendMeow::class.filter { !state.isMeowButtonLoading }.eventMap { sendMeow() },
                    Data.UpdateMeowCount::class.filter { !state.isMeowButtonLoading }.eventMap { updateMeowCount() },

                    // Request events transformation:
                    SendMeowRequestEvent::class.filter { it.isSuccess }.map { Data.UpdateMeowCount }
            )
        }
    }

    private fun generateAndLoadNewPopularList(): KittiesEvent {
        return Data.LoadPopularKittes.also { kittiesStorage.generateNewKittiesList() }
    }

    private fun loadAllData(): Observable<out KittiesEvent> {
        val events = listOf(
                Data.LoadTopKitten,
                Data.LoadNewKittiesCount,
                Data.LoadPopularKittes,
                Data.UpdateMeowCount
        )
        return Observable.fromIterable(events)
    }

    private fun loadTopKitten(): Observable<out KittiesEvent> {
        return fakeRequest(800L, kittiesStorage.getKittenOfTheWeek())
                .asRequestEvent(::TopKittenRequestEvent)
    }

    private fun loadNewKittiesCount(): Observable<out KittiesEvent> {
        return fakeRequest(1200L, kittiesStorage.getKittiesCount())
                .asRequestEvent(::NewKittiesCountRequestEvent)
    }

    private fun loadPopularKitties(): Observable<out KittiesEvent> {
        return fakeRequest(1600L, kittiesStorage.getPopularKitties())
                .asRequestEvent(::PopularKittiesRequestEvent)
    }

    private fun updateMeowCount(): Observable<out KittiesEvent> {
        return fakeRequest(500L, kittiesStorage.getMeowsCount())
                .asRequestEvent(::UpdateMeowCountRequestEvent)
    }

    private fun sendMeow(): Observable<out KittiesEvent> {
        return fakeRequest(500L, kittiesStorage.sendMeow())
                .asRequestEvent(::SendMeowRequestEvent)
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