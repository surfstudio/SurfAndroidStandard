package ru.surfstudio.android.build.utils

import ru.surfstudio.android.build.tasks.deploy_to_mirror.model.Commit

/**
 * Return parents set
 *
 * @param maxDistance - max depth for parenting
 */
fun <T : Commit> T.getParents(maxDistance: Int): Set<T> {
    val result = mutableSetOf<T>()
    val iCommits = mutableSetOf<T>()
    val iParents = mutableSetOf<T>()

    iCommits.add(this)

    for (i in 1..maxDistance) {
        if (iCommits.isEmpty()) break

        iCommits.forEach {
            iParents.addAll(it.parents as List<T>)
        }

        result.addAll(iParents)

        iCommits.clear()
        iCommits.addAll(iParents)
    }

    return result
}