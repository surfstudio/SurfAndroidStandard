package ru.surfstudio.android.build.tasks.bintray_tasks.release

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

private val currentDirectory: String = System.getProperty("user.dir")
private const val LINE = "________________________________________________________________"

//region commands
private const val GET_ALL_TAGS_COMMAND = "git tag --list"
private const val GET_LATEST_TAG_COMMAND = "git describe --tags" //http://bit.ly/2QlsLbo
private const val GET_LATEST_TAG_HASH_COMMAND = "git rev-list --tags --max-count=1"
private const val CHECKOUT_COMMAND = "git checkout"
private const val CHECKOUT_TAG_COMMAND = "$CHECKOUT_COMMAND tags"
private const val GET_CURRENT_BRANCH_NAME_COMMAND = "git rev-parse --abbrev-ref HEAD"
//endregion

/**
 * Base class for tasks of checking Bintray for release artifacts
 */
abstract class BaseCheckBintrayForReleaseTask : DefaultTask() {

    protected val sb = StringBuilder()

    @TaskAction
    fun check() {
        val workingDir = File(currentDirectory)
        val latestTagHash = CommandLineRunner.runCommandWithResult(GET_LATEST_TAG_HASH_COMMAND, workingDir)
        val latestTag = CommandLineRunner.runCommandWithResult(
                "$GET_LATEST_TAG_COMMAND $latestTagHash",
                workingDir
        )?.trim()
        val currentBranch = CommandLineRunner.runCommandWithResult(GET_CURRENT_BRANCH_NAME_COMMAND, workingDir)
        println("latest release version = $latestTag\n")

        val allTags = CommandLineRunner.runCommandWithResult(GET_ALL_TAGS_COMMAND, workingDir)
                ?.split("\n")

        allTags?.also { safeAllTags ->
            Bintray.getAllPackages().forEach { packageName ->
                // find release tag for artifact
                val checkoutTag = safeAllTags.firstOrNull { it.startsWith(packageName) } ?: latestTag
                if (checkoutTag != null) {
                    val isCheckoutTagForArtifact = checkoutTag.startsWith(packageName)
                    println("for $packageName checkoutTag = $checkoutTag")

                    val stableVersion = if (isCheckoutTagForArtifact) {
                        // get stable version from the artifact's release tag
                        checkoutTag.split("/").last()
                    } else {
                        // get stable version of the artifact for common tag
                        CommandLineRunner.runCommandWithResult("$CHECKOUT_TAG_COMMAND/$checkoutTag", workingDir)
                        val jsonComponents = JsonHelper.parseComponentsJson("$currentDirectory/$COMPONENTS_JSON_FILE_PATH")
                        val component = Components.parseComponent(jsonComponents, packageName)
                        CommandLineRunner.runCommandWithResult("$CHECKOUT_COMMAND $currentBranch", workingDir)
                        component?.baseVersion
                    }

                    if (stableVersion != null) {
                        // fail to parse the artifact's release tag
                        if (stableVersion.isEmpty()) {
                            throw GradleException("Invalid release tag format for $checkoutTag, " +
                                    "must be \"$packageName/STABLE-VERSION\""
                            )
                        }

                        performCheck(packageName, stableVersion, isCheckoutTagForArtifact)
                    } else {
                        println("NOT FOUND: Bintray contains a new artifact $packageName " +
                                "which is not found for tag $checkoutTag\n")
                    }
                    println(LINE)
                }
            } // Bintray.getAllPackages().forEach
        } // safeAllTags

        with(sb.toString()) {
            if (isNotEmpty()) {
                throw GradleException(this)
            }
        }
    }

    /**
     * Custom check
     *
     * @param packageName packageName from Bintray, which equals to component name
     * @param stableVersion stableVersion of artifact
     * @param isCheckoutTagForArtifact true if for current release artifact was used unique tag
     */
    abstract fun performCheck(
            packageName: String,
            stableVersion: String,
            isCheckoutTagForArtifact: Boolean
    )
}