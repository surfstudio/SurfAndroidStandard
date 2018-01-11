package ru.surfstudio.standard.app

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.github.anrwatchdog.ANRWatchDog

import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.Kit
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.app.log.RemoteLogger
import ru.surfstudio.android.core.util.ActiveActivityHolder
import ru.surfstudio.android.core.util.DefaultActivityLifecycleCallbacks
import ru.surfstudio.standard.app.dagger.ActiveActivityHolderModule
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.dagger.AppModule
import ru.surfstudio.standard.app.dagger.DaggerAppComponent
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Класс приложения
 */
class App : Application() {

    var appComponent: AppComponent? = null
        private set
    private val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initFabric()
        initAnrWatchDog()
        initInjector()
        initLog()
        initCalligraphy()
        registerActiveActivityListener()
    }

    /**
     * отслеживает ANR и отправляет в крашлитикс
     */
    private fun initAnrWatchDog() {
        ANRWatchDog().setReportMainThreadOnly()
                .setANRListener({ RemoteLogger.logError(it) })
                .start()
    }

    private fun initFabric() {
        val kits = arrayOf<Kit<*>>(Crashlytics.Builder().core(CrashlyticsCore.Builder().build()).build())
        Fabric.with(this, *kits)
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .activeActivityHolderModule(ActiveActivityHolderModule(activeActivityHolder))
                .build()
    }

    private fun initLog() {
        Logger.init()
    }

    /**
     * Инициализация шрифтовой библиотеки Calligraphy
     */
    private fun initCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(ru.surfstudio.android.core.R.attr.fontPath)
                .build())
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                activeActivityHolder.activity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                activeActivityHolder.clearActivity()
            }
        })
    }
}