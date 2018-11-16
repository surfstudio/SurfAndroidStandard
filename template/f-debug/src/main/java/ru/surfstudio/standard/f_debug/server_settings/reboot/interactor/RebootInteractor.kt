package ru.surfstudio.standard.f_debug.server_settings.reboot.interactor

import com.jakewharton.processphoenix.ProcessPhoenix
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class RebootInteractor @Inject constructor(
        private val activeActivityHolder: ActiveActivityHolder
) {
    /**
     * Полный перезапуск приложения
     *
     * @param activityRoute route активности, которая будет запущена после перезапуска
     */
    fun reboot(activityRoute: ActivityRoute) {
        if (activeActivityHolder.isExist) {
            val context = activeActivityHolder.activity!!
            val intent = activityRoute.prepareIntent(context)
            ProcessPhoenix.triggerRebirth(context, intent)
        }
    }
}