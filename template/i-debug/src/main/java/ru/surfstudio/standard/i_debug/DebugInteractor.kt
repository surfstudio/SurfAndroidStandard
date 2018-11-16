package ru.surfstudio.standard.i_debug

import android.app.Application
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_debug.storage.DebugServerSettingsStorage
import ru.surfstudio.standard.i_debug.storage.MemoryDebugStorage
import javax.inject.Inject

@PerApplication
class DebugInteractor @Inject constructor(
        private val memoryDebugStorage: MemoryDebugStorage,
        private val debugServerSettingsStorage: DebugServerSettingsStorage,
        private val application: Application
) {

    var isLeakCanaryEnabled: Boolean
        get() = memoryDebugStorage.isLeakCanaryEnabled
        set(value) {
            memoryDebugStorage.isLeakCanaryEnabled = value
        }

    var isChuckEnabled: Boolean
        get() = debugServerSettingsStorage.isChuckEnabled
        set(value) {
            memoryDebugStorage.isLeakCanaryEnabled = value
        }

    companion object {
        fun mustNotInitializeApp(application: Application): Boolean {
            return LeakCanary.isInAnalyzerProcess(application)
        }
    }

    fun onCreateApp() {
        if (memoryDebugStorage.isLeakCanaryEnabled) {
            LeakCanary.install(application)
        }
    }

    fun configureOkHttp(okHttpBuilder: OkHttpClient.Builder) {
        if (debugServerSettingsStorage.isChuckEnabled) {
            okHttpBuilder.addInterceptor(ChuckInterceptor(application))
        }
    }
}