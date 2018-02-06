package ru.surf.android.notification.interactor.push

import ru.surfstudio.android.core.domain.Unit
import java.io.Serializable

/**
 * Пуш без данных
 */
abstract class BaseNoDataNotificationTypeData : BaseNotificationTypeData<Unit>(), Serializable {

    override fun extractData(map: Map<String, String>): Unit {
        return Unit.INSTANCE
    }
}