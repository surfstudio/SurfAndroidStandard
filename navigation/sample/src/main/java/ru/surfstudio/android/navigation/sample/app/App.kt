package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import androidx.core.content.ContextCompat
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultFactory
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

class App : Application() {

    companion object {
        lateinit var executor: AppCommandExecutorWithResult
        lateinit var provider: ActivityNavigationProvider
        lateinit var resultObserver: ScreenResultObserver
    }

    override fun onCreate() {
        super.onCreate()
        val storage = FileScreenResultStorage(
                ContextCompat.getNoBackupFilesDir(this)!!.absolutePath
        )
        val screenResultEmmiter = ScreenResultBus(storage)
        val activityNavigationProviderCallbacks = ActivityNavigationProviderCallbacks(
                activityNavigatorFactory = ActivityNavigatorWithResultFactory()
        )
        registerActivityLifecycleCallbacks(activityNavigationProviderCallbacks)
        provider = activityNavigationProviderCallbacks
        executor = AppCommandExecutorWithResult(screenResultEmmiter, activityNavigationProviderCallbacks)

        DefaultAnimations.fragment = SlideAnimations()
        DefaultAnimations.tab = FadeAnimations()
    }
}