package ru.surfstudio.android.build.model.release_notes

/**
 * Represent release notes version
 */
data class ReleaseNotesVersion(
        val version: String,
        val libraries: List<ReleaseNotesLibrary>
) {

    val isEmpty: Boolean get() = libraries.isEmpty() && libraries.all { it.items.isEmpty() }
}