package ru.surfstudio.android.firebase.sample.app.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.ui.common.notification.PushHandleStrategyFactory
import ru.surfstudio.android.notification.NotificationManager
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.notificationManager
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.DefaultPushHandler
import ru.surfstudio.android.notification.ui.notification.PushHandler

@Module
class NotificationModule {

    @Provides
    @PerApplication
    fun providePushInteractor() = PushInteractor()

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

    @Provides
    @PerApplication
    fun providePushHandleStrategyFactory(): AbstractPushHandleStrategyFactory =
            PushHandleStrategyFactory

    @Provides
    @PerApplication
    fun providesNotificationManager(
            pushHandler: PushHandler,
            pushHandleStrategyFactory: AbstractPushHandleStrategyFactory
    ): NotificationManager =
            notificationManager {
                setPushHandler(pushHandler)
                setPushHandleStrategyFactory(pushHandleStrategyFactory)
            }
}