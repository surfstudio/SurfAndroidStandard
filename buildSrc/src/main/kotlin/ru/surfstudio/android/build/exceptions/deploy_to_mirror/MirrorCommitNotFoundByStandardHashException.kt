package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

class MirrorCommitNotFoundByStandardHashException(hash: String) : GradleException(
        "Commit not found in mirror with hash = $hash"
)