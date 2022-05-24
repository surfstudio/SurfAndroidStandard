package ru.surfstudio.android.build.exceptions.branch

import org.gradle.api.GradleException

class BranchCanNotBeDefinedException(
        commit: String,
        branches: List<String>,
        refs: List<String>
) : GradleException(
        """Can't define branch for commit : $commit
            | branches : $branches
            | refs : $refs
        """.trimMargin()
)