package ru.surfstudio.standard.f_debug

import okhttp3.OkHttpClient
import javax.inject.Inject

class DebugInteractor @Inject constructor() {

    var isTestServerEnabled = false

    fun onCreateApp(icon: Int) {/* no action */}

    fun mustNotInitializeApp() = false

    fun configureOkHttp(builder: OkHttpClient.Builder){/* no action */}
}