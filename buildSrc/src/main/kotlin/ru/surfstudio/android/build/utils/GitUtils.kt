package ru.surfstudio.android.build.utils

import org.eclipse.jgit.revwalk.RevCommit
import java.lang.Exception

private const val STANDARD_COMMIT_HASH_PREFIX = "ANDROID-STANDARD-COMMIT=\""
private const val STANDARD_COMMIT_HASH_POSTFIX = "\""

/**
 * Return parents set
 *
 * @param maxDistance - max depth for parenting
 */
fun RevCommit.getAllParents(maxDistance: Int): Set<RevCommit> {
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

/**
 * Get standard commit hash from [RevCommit]'s message
 */
val RevCommit.standardHash: String
    get() {
        return try {
            shortMessage.substringAfter(STANDARD_COMMIT_HASH_PREFIX, EMPTY_STRING)
                    .substringBefore(STANDARD_COMMIT_HASH_POSTFIX)
        } catch (e: Exception) {
            EMPTY_STRING
        }
    }