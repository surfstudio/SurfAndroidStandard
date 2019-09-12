package ru.surfstudio.standard.application.notification

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.impl.DefaultPushHandler
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory

@Module
class NotificationModule {

    @Provides
    @PerApplication
    fun providePushInteractor() = PushInteractor()

    @Provides
    @PerApplication
    fun providePushHandleStrategyFactory(): AbstractPushHandleStrategyFactory =
            PushHandleStrategyFactory

    @Provides
    @PerApplication
    fun providePushHandler(
            activeActivityHolder: ActiveActivityHolder,
            pushHandleStrategyFactory: AbstractPushHandleStrategyFactory,
            pushInteractor: PushInteractor
    ): PushHandler =
            DefaultPushHandler(
                    activeActivityHolder,
                    pushHandleStrategyFactory,
                    pushInteractor
            )

}