package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import androidx.core.content.ContextCompat
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultFactory
import ru.surfstudio.android.navigation.observer.storage.file.FileRouteStorage
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutorWithResult
        lateinit var provider: ActivityNavigationProvider
        lateinit var resultObserver: ScreenResultObserver
    }

    override fun onCreate() {
        super.onCreate()
        initResultObserver()
        initAnimations()
    }

    private fun initExecutor(screenResultEmitter: ScreenResultEmitter) {
        val routeStorage = FileRouteStorage(noBackupFilesDir())
        val activityNavigationProvider = ActivityNavigationProviderCallbacks(
            activityNavigatorFactory = ActivityNavigatorWithResultFactory(
                routeStorage = routeStorage,
                screenResultEmitter = screenResultEmitter
            )
        )
        registerActivityLifecycleCallbacks(activityNavigationProvider)
        provider = activityNavigationProvider
        navigator = AppCommandExecutorWithResult(screenResultEmitter, activityNavigationProvider)
    }

    private fun initResultObserver() {
        val storage = FileScreenResultStorage(noBackupFilesDir())
        val resultBus = ScreenResultBus(storage)
        resultObserver = resultBus
        initExecutor(resultBus)
    }

    private fun initAnimations() {
        DefaultAnimations.fragment = SlideAnimations() //animations for all fragment changes
        DefaultAnimations.tab = FadeAnimations() //animations for tab changes
    }

    private fun noBackupFilesDir(): String {
        return ContextCompat.getNoBackupFilesDir(this)!!.absolutePath
    }
}