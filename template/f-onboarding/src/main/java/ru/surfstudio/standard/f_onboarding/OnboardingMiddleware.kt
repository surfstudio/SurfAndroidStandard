package ru.surfstudio.standard.f_onboarding

import io.reactivex.Observable
import javax.inject.Inject
import ru.surfstudio.standard.f_onboarding.OnboardingEvent.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.i_onboarding.OnBoardingStorage
import ru.surfstudio.standard.ui.mvi.navigation.base.NavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.builder
import ru.surfstudio.standard.ui.mvi.navigation.extension.finish
import ru.surfstudio.standard.ui.mvi.navigation.extension.finishAffinity
import ru.surfstudio.standard.ui.mvi.navigation.extension.start
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute

@PerScreen
internal class OnboardingMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val onBoardingStorage: OnBoardingStorage,
) : BaseMiddleware<OnboardingEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<OnboardingEvent>) = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                Input.SkipBtnClicked::class mapTo { closeOnboardingClick(false) },
                Input.RemindLaterBtnClicked::class mapTo { closeOnboardingClick(true) }
        )
    }

    private fun closeOnboardingClick(isRemindLater: Boolean): OnboardingEvent {
        onBoardingStorage.shouldShowOnBoardingScreen = isRemindLater
        return Navigation().builder()
                .finishAffinity()
                .start(MainActivityRoute())
                .build()
    }
}
