package ru.surfstudio.android.build.utils

import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.GradleException

const val STANDARD_COMMIT_HASH_PREFIX = "("
const val STANDARD_COMMIT_HASH_POSTFIX = ")"

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
val RevCommit.mirrorStandardHash: String
    get() {
        return try {
            shortMessage.substringAfterLast(STANDARD_COMMIT_HASH_PREFIX, EMPTY_STRING)
                    .substringBefore(STANDARD_COMMIT_HASH_POSTFIX)
        } catch (e: Exception) {
            EMPTY_STRING
        }
    }

val RevCommit.standardHash: String get() = name.substring(0, 8)

/**
 * Check commit is merge
 */
val RevCommit.isMergeCommit: Boolean get() = throw GradleException()
//val RevCommit.isMergeCommit: Boolean get() = shortMessage.startsWith("Merge branch")

val DiffEntry.type: DiffEntry.ChangeType get() = changeType ?: DiffEntry.ChangeType.ADD

val RevCommit.shortHash: String get() = name.substring(0, 8)