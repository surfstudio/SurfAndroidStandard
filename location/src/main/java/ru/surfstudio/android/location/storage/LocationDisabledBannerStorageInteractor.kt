package ru.surfstudio.android.location.storage

import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Интерактор для работы с хранилищем количества показов баннера c просьбой включить геолокацию
 */
@PerApplication
class LocationDisabledBannerStorageInteractor @Inject constructor(private val locationDisabledStorage: LocationDisabledStorage) {

    val CLOSING_BANNER_COUNT_THRESHOLD = 1

    /**
     * Увеличивает счетчик
     */
    fun increaseShowCounter() {
        locationDisabledStorage.incBannerShowCount()
    }


    /**
     * Минимизирует баннер при количестве закрытий больше двух раз
     */
    fun shouldMinimizeBanner() = locationDisabledStorage.getBannerShowCount() >= CLOSING_BANNER_COUNT_THRESHOLD
}