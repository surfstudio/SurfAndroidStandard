package ru.surfstudio.android.build.model

data class BintrayRepoLatestVersion(
        val name: String,
        val desc: String?,
        val packageName: String,
        val repo: String,
        val owner: String,
        val published: Boolean,
        val created: String,
        val updated: String,
        val released: String,
        val ordinal: Double,
        val githubReleaseNotesFile: String?,
        val githubUseTagReleaseNotes: Boolean,
        val vcsTag: String?,
        val ratingCount: Int
)