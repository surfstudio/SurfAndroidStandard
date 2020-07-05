package ru.surfstudio.android.build.tasks.bintray_tasks.release

fun getArtifactName(tag: String): String? {
    return if (tag.contains('/')) {
        tag.split('/').first()
    } else {
        null
    }
}

fun getMaxVersion(tags: List<String>) =
        getMaxVersion(tags.map { getArtifactVersionNumbers(it) })
                .joinToString(separator = ".")

fun getMaxVersion(versions: List<List<Int>>): List<Int> {
    val versionSize = versions.first().size
    var maxVersions = versions
    for (i in 0 until versionSize) {
        val maxAtCurrentPosition = maxVersions.map { it[i] }.max()
        maxVersions = maxVersions.filter { it[i] == maxAtCurrentPosition }
    }
    return maxVersions.first()
}

fun getArtifactVersionNumbers(tag: String) = getArtifactVersion(tag)
        .split('.')
        .map(String::toInt)

fun getArtifactVersion(tag: String) = tag.split('/').last()
