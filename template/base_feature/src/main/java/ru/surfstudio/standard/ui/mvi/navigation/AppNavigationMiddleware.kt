package ru.surfstudio.standard.ui.mvi.navigation

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.freezer.LifecycleSubscriptionFreezer
import ru.surfstudio.android.core.mvi.impls.ui.freezer.freeze
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.rx.extension.scheduler.MainThreadImmediateScheduler
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.standard.ui.mvi.navigation.base.NavigationMiddleware

/**
 * Проектная реализация middleware для осуществления навигации на основе команд.
 * Под капотом использует [NavigationCommandExecutor], который исполняет приходящие команды навигации.
 */
class AppNavigationMiddleware(
        private val navigationExecutor: NavigationCommandExecutor,
        private val lifecycleSubscriptionFreezer: LifecycleSubscriptionFreezer
) : NavigationMiddleware {

    override fun transform(
            eventStream: Observable<NavCommandsEvent>
    ): Observable<out NavCommandsEvent> {
        return eventStream
                .freeze(lifecycleSubscriptionFreezer)
                .observeOn(MainThreadImmediateScheduler)
                .flatMap(::executeNavigationCommands)
    }

    private fun executeNavigationCommands(
            event: NavCommandsEvent
    ): Observable<out NavCommandsEvent> {
        navigationExecutor.execute(event.commands)
        return Observable.empty()
    }
}
