package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.model.json.LibJson

/**
 * Represent information about library with version
 */
data class LibraryForCheck(
        val name: String,
        val directory: String,
        var thirdPartyDependencies: List<DependencyForCheck> = listOf(),
        var androidStandardDependencies: List<DependencyForCheck> = listOf()
) {
    companion object {

        fun create(lib: LibJson)
                : LibraryForCheck {
            return LibraryForCheck(lib.name, lib.dir)
        }
    }
}