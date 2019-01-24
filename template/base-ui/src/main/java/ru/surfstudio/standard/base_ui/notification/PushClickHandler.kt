package ru.surfstudio.standard.base_ui.notification

import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.domain.notification.Notification
import javax.inject.Inject

@PerApplication
class PushClickHandler @Inject constructor() {

    private val pushClickSubject = PublishSubject.create<Notification>()

    val pushclickObservable: Observable<Notification> = pushClickSubject

    internal fun onPushClick(notification: Notification) {
        pushClickSubject.onNext(notification)
    }
}