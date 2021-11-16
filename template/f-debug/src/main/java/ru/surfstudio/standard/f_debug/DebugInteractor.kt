package ru.surfstudio.standard.f_debug

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.codemonkeylabs.fpslibrary.TinyDancer
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import leakcanary.LeakCanary
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.f_debug.network.DelayInterceptor
import ru.surfstudio.standard.f_debug.notification.DebugNotificationBuilder
import ru.surfstudio.standard.f_debug.scalpel.DebugScalpelManager
import ru.surfstudio.standard.f_debug.server_settings.reboot.interactor.DebugRebootInteractor
import ru.surfstudio.standard.f_debug.storage.DebugServerSettingsStorage
import ru.surfstudio.standard.f_debug.storage.DebugUiToolsStorage
import ru.surfstudio.standard.f_debug.storage.MemoryDebugStorage
import ru.surfstudio.standard.f_debug.storage.ToolsDebugStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerApplication
class DebugInteractor @Inject constructor(
        private val activeActivityHolder: ActiveActivityHolder,
        private val memoryDebugStorage: MemoryDebugStorage,
        private val debugServerSettingsStorage: DebugServerSettingsStorage,
        private val debugUiToolsStorage: DebugUiToolsStorage,
        private val toolsDebugStorage: ToolsDebugStorage,
        private val application: Application,
        private val rebootInteractor: DebugRebootInteractor
) {

    private val serverChangedPublishSubject = PublishSubject.create<Unit>()

    private var firstActivityOpeningDisposable = Disposables.disposed()

    fun observeNeedClearSession(): Observable<Unit> {
        return serverChangedPublishSubject
    }

    //region Настройки LeakCanary
    var isLeakCanaryEnabled: Boolean
        get() = memoryDebugStorage.isLeakCanaryEnabled
        set(value) {
            memoryDebugStorage.isLeakCanaryEnabled = value
            toggleLeakCanary(value)
        }
    //endregion

    //region UI-tools
    var isFpsEnabled: Boolean
        get() = debugUiToolsStorage.isFpsEnabled
        set(value) {
            debugUiToolsStorage.isFpsEnabled = value
        }
    //endregion

    //region Tools
    var isStethoEnabled: Boolean
        get() = toolsDebugStorage.isStethoEnabled
        set(value) {
            toolsDebugStorage.isStethoEnabled = value
        }
    //endregion

    //region Настройки сервера
    var isChuckEnabled: Boolean
        get() = debugServerSettingsStorage.isChuckEnabled
        set(value) {
            debugServerSettingsStorage.isChuckEnabled = value
        }

    var isTestServerEnabled: Boolean
        get() = debugServerSettingsStorage.isTestServerEnabled
        set(value) {
            debugServerSettingsStorage.isTestServerEnabled = value
            serverChangedPublishSubject.onNext(Unit)
        }

    /**
     * Задержка между запросами на сервер в миллисекундах
     */
    var requestDelay: Long
        get() = debugServerSettingsStorage.requestDelay
        set(value) {
            debugServerSettingsStorage.requestDelay = value
        }

    /**
     * Добавляет [ChuckerInterceptor], [StethoInterceptor] в [OkHttpClient] если в настройках включено
     */
    fun configureOkHttp(okHttpBuilder: OkHttpClient.Builder) {
        if (debugServerSettingsStorage.isChuckEnabled) {
            okHttpBuilder.addInterceptor(
                ChuckerInterceptor.Builder(application)
                    .collector(ChuckerCollector(application))
                    .alwaysReadResponseBody(true)
                    .build()
            )
        }

        if (toolsDebugStorage.isStethoEnabled) {
            okHttpBuilder.addNetworkInterceptor(StethoInterceptor())
        }

        okHttpBuilder.addInterceptor(DelayInterceptor(requestDelay))
    }
    //endregion

    /**
     * Нужно вызвать в [Application.onCreate]
     */
    fun onCreateApp(icon: Int) {
        firstActivityOpeningDisposable = activeActivityHolder
                .activityObservable
                .subscribe { handleFirstActivityOpening(icon) }
        DebugScalpelManager.init(application)
        toggleLeakCanary(memoryDebugStorage.isLeakCanaryEnabled)

        if (toolsDebugStorage.isStethoEnabled) {
            Stetho.initializeWithDefaults(application)
        }

        if (debugUiToolsStorage.isFpsEnabled) {
            TinyDancer.create().show(application)
        }
    }

    /**
     * Включает/выключает отслеживание утечек памяти через LeakCanary.
     * */
    private fun toggleLeakCanary(isEnabled: Boolean) {
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = isEnabled)
    }

    fun reboot(route: ActivityRoute) {
        rebootInteractor.reboot(route)
    }

    private fun handleFirstActivityOpening(icon: Int) {
        firstActivityOpeningDisposable.dispose()
        DebugNotificationBuilder.showDebugNotification(application, icon)
    }
}