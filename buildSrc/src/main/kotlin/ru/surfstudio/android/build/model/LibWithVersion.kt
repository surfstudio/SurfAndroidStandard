package ru.surfstudio.android.build.model

import com.beust.klaxon.Json

/**
* Представляет информацию о библиотеках, которые мужны компоненту с версиями
*/
data class LibWithVersion(
        val name: String = "",
        var thirdPartyDependencies: List<DepWithVersion> = listOf(),
        var androidStandardDependencies: List<DepWithVersion> = listOf()
) {}