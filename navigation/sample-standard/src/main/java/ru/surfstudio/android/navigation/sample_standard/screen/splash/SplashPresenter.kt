package ru.surfstudio.android.navigation.sample_standard.screen.splash

import android.util.Log
import io.reactivex.Completable
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.main.MainRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class SplashPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val executor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        Log.d("111111", "first load")
        subscribe(
                Completable.timer(1L, TimeUnit.SECONDS),
                {
                    Log.d("111111", " completed ")
                    executor.execute(Start(MainRoute()))
                },
                {}
        )
    }
}