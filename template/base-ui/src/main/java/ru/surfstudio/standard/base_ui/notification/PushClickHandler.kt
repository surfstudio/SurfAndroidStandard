package ru.surfstudio.standard.base_ui.notification

import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class PushClickHandler @Inject constructor() {

    private val pushClickSubject = PublishSubject.create<Intent>()

    val pushclickObservable: Observable<Intent> = pushClickSubject


    internal fun onPushClick(intent: Intent) {
        pushClickSubject.onNext(intent)
    }
}