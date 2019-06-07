package ru.surfstudio.android.build.model.release_notes

/**
 * Represent info about release notes document
 */
data class ReleaseNotesInfo(
        val toc: Boolean,
        val title: String,
        val versions: List<ReleaseNotesVersion>
)