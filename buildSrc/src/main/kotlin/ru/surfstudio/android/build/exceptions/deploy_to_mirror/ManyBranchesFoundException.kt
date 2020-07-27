package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException

class ManyBranchesFoundException(commitId: String, list: List<String>) : GradleException(
        "Too many branches found for commit : $commitId \nBranches : $list\nTry merge or delete side branches."
)