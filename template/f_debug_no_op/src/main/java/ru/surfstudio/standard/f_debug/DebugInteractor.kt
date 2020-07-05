package ru.surfstudio.standard.f_debug

import io.reactivex.Observable
import okhttp3.OkHttpClient
import javax.inject.Inject

class DebugInteractor @Inject constructor() {

    var isTestServerEnabled = false

    fun observeNeedClearSession(): Observable<Unit> = Observable.empty()

    fun onCreateApp(icon: Int) {/* no action */}

    fun mustNotInitializeApp() = false

    fun configureOkHttp(builder: OkHttpClient.Builder){/* no action */}
}