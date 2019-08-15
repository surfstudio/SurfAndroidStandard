package ru.surfstudio.android.build.utils

fun List<String>.extractBranchNames() = map {
    it.substringAfter("refs/")
            .substringAfter("heads/")
            .substringAfter("origin/")
}
        .filter { it != "HEAD" }
        .distinct()