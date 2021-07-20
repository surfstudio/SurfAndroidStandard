package ru.surfstudio.android.navigation.sample_standard.screen.splash

import io.reactivex.Completable
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.guide.GuideRoute
import ru.surfstudio.android.navigation.sample_standard.screen.main.MainRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class SplashPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val executor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {


        subscribe(
                Completable.timer(1L, TimeUnit.SECONDS),
                {
                    executor.execute(listOf(
                            Finish(),
                            Start(MainRoute(1)),
                            Add(GuideRoute())
                    ))
                },
                {}
        )
    }
}