package ru.surfstudio.standard.f_debug.server_settings.reboot.interactor

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class RebootInteractor @Inject constructor(private val context: Context) {
    /**
     * Полный перезапуск приложения
     *
     * @param activityRoute route активности, которая будет запущена после перезапуска
     */
    fun reboot(activityRoute: ActivityRoute) {
        ProcessPhoenix.triggerRebirth(context, activityRoute.prepareIntent(context))
    }
}