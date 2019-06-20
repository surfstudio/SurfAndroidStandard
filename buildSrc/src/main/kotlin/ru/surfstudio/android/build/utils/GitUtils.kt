package ru.surfstudio.android.build.utils

import org.eclipse.jgit.revwalk.RevCommit

/**
 * Return parents set
 *
 * @param maxDistance - max depth for parenting
 */
fun RevCommit.getParents(maxDistance: Int): Set<RevCommit> {
    val result = mutableSetOf<RevCommit>()
    val iCommits = mutableSetOf<RevCommit>()
    val iParents = mutableSetOf<RevCommit>()

    iCommits.add(this)

    for (i in 1..maxDistance) {
        if (iCommits.isEmpty()) break

        iCommits.forEach {
            iParents.addAll(it.parents)
        }

        result.addAll(iParents)

        iCommits.clear()
        iCommits.addAll(iParents)
    }

    return result
}