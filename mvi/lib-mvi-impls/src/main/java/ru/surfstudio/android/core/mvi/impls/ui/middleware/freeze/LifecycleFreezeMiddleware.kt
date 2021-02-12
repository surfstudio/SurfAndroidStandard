package ru.surfstudio.android.core.mvi.impls.ui.middleware.freeze

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * Middleware, содержащая методы для заморозки событий до наступления
 * определенной стадии жизненного цикла экрана.
 */
interface LifecycleFreezeMiddleware<T : Event> : RxMiddleware<T> {

    val screenState: ScreenState

    /**
     * Заморозка событий до момента наступления resumed-состояния экрана
     *
     * @param eventStream список событий экрана
     * @param onResumeObservableCreator - лямбда, лениво создающая observable,
     * который будет выполняться в при достижении resumed-состояния.
     *
     * @return [Observable]<[T]>
     */
    fun <T : Event> freezeUntilResume(
            eventStream: Observable<T>,
            onResumeObservableCreator: () -> Observable<out T>
    ): Observable<out T> = freezeUntil(LifecycleStage.RESUMED, eventStream, onResumeObservableCreator)

    /**
     * Заморозка событий до момента наступления [lifecycleStage]-состояния экрана
     *
     * @param lifecycleStage стадия жизненного цикла, при наступлении которой мы выполним действие
     * @param eventStream список событий экрана
     * @param onResumeObservableCreator - лямбда, лениво создающая observable,
     * который будет выполняться в при достижении resumed-состояния.
     *
     * @return [Observable]<[T]>
     */
    fun <T : Event> freezeUntil(
            lifecycleStage: LifecycleStage,
            eventStream: Observable<T>,
            onResumeObservableCreator: () -> Observable<out T>
    ): Observable<out T> {
        val currentStage = screenState.lifecycleStage
        return if (currentStage == lifecycleStage) {
            onResumeObservableCreator()
        } else {
            eventStream
                    .filterIsInstance<LifecycleEvent>()
                    .takeUntil { it.stage == lifecycleStage }
                    .takeLast(1)
                    .flatMap { onResumeObservableCreator() }
        }
    }

}