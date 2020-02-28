package ru.surfstudio.android.build.model.release_notes

import ru.surfstudio.android.build.model.Component

/**
 * Represent info about release notes document
 */
data class ReleaseNotesInfo(
        val component: Component,
        val toc: Boolean,
        val title: String,
        val versions: List<ReleaseNotesVersion>
)