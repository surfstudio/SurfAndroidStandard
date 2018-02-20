package ru.surfstudio.android.notification.ui.notification

import android.content.Context
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import javax.inject.Inject

/**
 * Выполняем необходимые действия при пуше на ui
 * Определяет нужно ли послать событие в пуш интерактор, либо же
 * создать нотификацию открытия экрана по пушу
 */
@PerApplication
class PushHandler @Inject constructor(private val activeActivityHolder: ActiveActivityHolder,
                                      private val pushHandleStrategyFactory: AbstractPushHandleStrategyFactory,
                                      private val pushInteractor: PushInteractor) {

    internal fun handleMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        val activity = activeActivityHolder.activity

        val pushHandleStrategy = pushHandleStrategyFactory
                .createByData(data)
        Logger.i("PushHandler пуш $activity \n $title \n pushStrategy = $pushHandleStrategy")
        pushHandleStrategy?.handle(activity ?: context, pushInteractor, title, body, data)
    }
}