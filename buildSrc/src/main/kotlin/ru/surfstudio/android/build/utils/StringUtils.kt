package ru.surfstudio.android.build.utils

fun List<String>.extractBranchNames() = map { it.substringAfterLast("/") }
        .filter { it != "HEAD" }
        .distinct()