package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import androidx.core.content.ContextCompat
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
        lateinit var provider: ActivityNavigationProvider
        lateinit var resultObserver: ScreenResultObserver
    }

    override fun onCreate() {
        super.onCreate()
        initResultObserver()
        initAnimations()
    }

    private fun initExecutor(screenResultEmitter: ScreenResultEmitter) {
        val callbacksSupplier = ActivityNavigationProviderCallbacks()
        registerActivityLifecycleCallbacks(callbacksSupplier)
        provider = callbacksSupplier
        navigator = AppCommandExecutorWithResult(screenResultEmitter, callbacksSupplier)
    }

    private fun initResultObserver() {
        val filesDir = ContextCompat.getNoBackupFilesDir(this)!!.absolutePath
        val storage = FileScreenResultStorage(filesDir)
        val resultBus = ScreenResultBus(storage)
        resultObserver = resultBus
        initExecutor(resultBus)
    }

    private fun initAnimations() {
        DefaultAnimations.fragment = SlideAnimations() //animations for all fragment changes
        DefaultAnimations.tab = FadeAnimations() //animations for tab changes
    }
}