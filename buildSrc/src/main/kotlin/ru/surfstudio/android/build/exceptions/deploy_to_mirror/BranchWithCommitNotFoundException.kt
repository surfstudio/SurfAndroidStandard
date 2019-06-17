package ru.surfstudio.android.build.exceptions.deploy_to_mirror

/**
 * Branch with commit not found
 */
class BranchWithCommitNotFoundException(
        repositoryName: String,
        commit: String
) : BaseDeployToMirrorException(
        repositoryName,
        "Branch with commit \"$commit\" not found."
)