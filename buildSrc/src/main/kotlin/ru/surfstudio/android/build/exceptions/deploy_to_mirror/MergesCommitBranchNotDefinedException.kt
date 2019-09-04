package ru.surfstudio.android.build.exceptions.deploy_to_mirror

import org.gradle.api.GradleException
import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.CommitWithBranch

class MergesCommitBranchNotDefinedException(
        commitWithBranch: CommitWithBranch,
        smallList: List<String>,
        bigList: List<String>
) : GradleException(
        "Branch for Commit \n" +
                "$commitWithBranch\n" +
                "can't be defined by :\n" +
                "smallList: $smallList\n" +
                "bigList: $bigList"
)