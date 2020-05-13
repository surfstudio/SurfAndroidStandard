package ru.surfstudio.android.firebase.sample.app.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.ui.common.notification.PushHandleStrategyFactory
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.impl.DefaultPushHandler
import ru.surfstudio.android.notification.interactor.push.PushNotificationsListener
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory

@Module
class NotificationModule {

    @Provides
    @PerApplication
    fun providePushNotificationsListener() = PushNotificationsListener()

    @Provides
    @PerApplication
    fun providePushHandleStrategyFactory(): AbstractPushHandleStrategyFactory =
            PushHandleStrategyFactory

    @Provides
    @PerApplication
    fun providePushHandler(
            activeActivityHolder: ActiveActivityHolder,
            pushHandleStrategyFactory: AbstractPushHandleStrategyFactory,
            pushNotificationsListener: PushNotificationsListener
    ): PushHandler =
            DefaultPushHandler(
                    activeActivityHolder,
                    pushHandleStrategyFactory,
                    pushNotificationsListener
            )

}