package ru.surfstudio.android.build.model.json.bintray

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.model.BintrayRepoLatestVersion
import ru.surfstudio.android.build.utils.Transformable

/**
 * Response model of latest version info of artifact in bintray
 */
@Deprecated("Use Artifactory or Maven Central after Bintray sunset")
data class BintrayRepoLatestVersionJson(
    @SerializedName("name") val name: String,
    @SerializedName("desc") val desc: String?,
    @SerializedName("package") val packageName: String,
    @SerializedName("repo") val repo: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("published") val published: Boolean,
    @SerializedName("created") val created: String,
    @SerializedName("updated") val updated: String,
    @SerializedName("released") val released: String,
    @SerializedName("ordinal") val ordinal: Double,
    @SerializedName("github_release_notes_file") val githubReleaseNotesFile: String?,
    @SerializedName("github_use_tag_release_notes") val githubUseTagReleaseNotes: Boolean,
    @SerializedName("vcs_tag") val vcsTag: String?,
    @SerializedName("rating_count") val ratingCount: Int
) : Transformable<BintrayRepoLatestVersion> {

    override fun transform() = BintrayRepoLatestVersion(
        name = name,
        desc = desc,
        packageName = packageName,
        repo = repo,
        owner = owner,
        published = published,
        created = created,
        updated = updated,
        released = released,
        ordinal = ordinal,
        githubReleaseNotesFile = githubReleaseNotesFile,
        githubUseTagReleaseNotes = githubUseTagReleaseNotes,
        vcsTag = vcsTag,
        ratingCount = ratingCount
    )
}