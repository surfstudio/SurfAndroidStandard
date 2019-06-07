package ru.surfstudio.android.build.model.release_notes

/**
 * Represent release notes version
 */
data class ReleaseNotesVersion(
        val version: String,
        val libraries: List<ReleaseNotesLibrary>
)