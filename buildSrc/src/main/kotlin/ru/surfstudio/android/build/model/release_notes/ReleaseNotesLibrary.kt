package ru.surfstudio.android.build.model.release_notes

/**
 * Represent release notes library section
 */
data class ReleaseNotesLibrary(
        val name: String,
        val items: List<ReleaseNotesItem>
)