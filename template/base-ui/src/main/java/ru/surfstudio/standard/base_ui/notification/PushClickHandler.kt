package ru.surfstudio.standard.base_ui.notification

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.domain.notification.Notification
import javax.inject.Inject

/**
 * Обработчик нажатия пушов
 *
 * подписаться нужному обработчику
 */
@PerApplication
class PushClickHandler @Inject constructor() {

    private val pushClickSubject = PublishSubject.create<Notification>()

    val pushClickObservable = pushClickSubject.hide()

    internal fun onPushClick(notification: Notification) {
        pushClickSubject.onNext(notification)
    }
}