package ru.surfstudio.standard.ui.mvi.navigation

import android.annotation.SuppressLint
import android.content.Context
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable
import javax.inject.Inject

/**
 * Объект для проверки возможности интента на исполнение.
 *
 */
@PerApplication
class IntentChecker @Inject constructor(private val context: Context) {

    /**
     * Функция проверки роута на возможность исполнения
     *
     * @param route роут, который проверяется
     * @param successEvent событие в случае успеха проверки
     * @param errorEvent событие для случая, когда приложения для исполнения роута не найдено
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun <E : Event, T : Serializable> check(
            route: ActivityWithResultRoute<T>,
            successEvent: E,
            errorEvent: E
    ): E =
            if (route.createIntent(context).resolveActivity(context.packageManager) != null) {
                successEvent
            } else {
                errorEvent
            }
}