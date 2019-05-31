package ru.surfstudio.android.build.model

/**
 * Represent information about library with version
 */
data class LibWithVersion(
        val name: String,
        var thirdPartyDependencies: List<DepWithVersion>,
        var androidStandardDependencies: List<DepWithVersion>
) {}