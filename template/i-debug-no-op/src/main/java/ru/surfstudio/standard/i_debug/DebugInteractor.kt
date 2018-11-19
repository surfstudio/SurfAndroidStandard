package ru.surfstudio.standard.i_debug

import okhttp3.OkHttpClient
import javax.inject.Inject

class DebugInteractor @Inject constructor() {
    fun onCreateApp() {/* no action */}

    fun mustNotInitializeApp() = false

    fun configureOkHttp(builder: OkHttpClient.Builder){/* no action */}
}