package ru.surfstudio.standard.i_network.cache

import ru.surfstudio.standard.i_network.network.cache.SimpleCacheInfo
import javax.inject.Inject

/**
 * Хранилище информации о запросах, требующих кэширования
 */
class SimpleCacheInfoStorage @Inject constructor() {

    /**
     * Заполняется [SimpleCacheInfo].
     */
    val simpleCaches: Collection<SimpleCacheInfo> = listOf(

    )
}
