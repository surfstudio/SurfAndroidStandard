package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

class RevCommitNotFoundException(commitHash: String) : GradleException(
        "RevCommit with hash \"$commitHash\" not found"
)