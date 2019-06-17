package ru.surfstudio.android.build.tasks.deploy_to_mirror

import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.LocalMirrorRepository
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository

/**
 * Git diff manager
 */
class GitDiffManager(
        val standardRepository: StandardRepository,
        val mirrorRepository: LocalMirrorRepository
) {

    /**
     * Compare branch in repositories and do their same
     *
     * @param branchName - branch with commits
     * @param commitFrom - start commit. Can be empty, then branch just copy
     * @param commitTo - finish commit
     */
    //TODO изменить название
    fun resolve(branchName: String, commitFrom: String, commitTo: String) {

    }
}