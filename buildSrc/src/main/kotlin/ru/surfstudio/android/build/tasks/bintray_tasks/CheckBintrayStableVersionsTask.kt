package ru.surfstudio.android.build.tasks.bintray_tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.bintray.Bintray
import ru.surfstudio.android.build.tasks.changed_components.CommandLineRunner
import ru.surfstudio.android.build.utils.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File
import java.lang.StringBuilder

private val currentDirectory: String = System.getProperty("user.dir")

//region commands
private const val GET_LATEST_TAG_COMMAND = "git describe --tags" //http://bit.ly/2QlsLbo
private const val GET_LATEST_TAG_HASH_COMMAND = "git rev-list --tags --max-count=1"
private const val CHECKOUT_COMMAND = "git checkout"
private const val CHECKOUT_TAG_COMMAND = "$CHECKOUT_COMMAND tags"
private const val GET_CURRENT_BRANCH_NAME_COMMAND = "git rev-parse --abbrev-ref HEAD"
//endregion

/**
 * Check if all artifacts in bintray have stable version as latest.
 *
 * WARNING! The task overrides [Components.value] using old components versions from git history.
 * Components must be initialized with actual values after this task, if needed.
 */
open class CheckBintrayStableVersionsTask : DefaultTask() {

    @TaskAction
    fun check() {
        val workingDir = File(currentDirectory)
        val latestTagHash = CommandLineRunner.runCommandWithResult(GET_LATEST_TAG_HASH_COMMAND, workingDir)
        val latestTag = CommandLineRunner.runCommandWithResult("$GET_LATEST_TAG_COMMAND $latestTagHash", workingDir)
        val currentBranch = CommandLineRunner.runCommandWithResult(GET_CURRENT_BRANCH_NAME_COMMAND, workingDir)
        println("latest release version = $latestTag")

        // Get stable versions of components for latest tag.
        CommandLineRunner.runCommandWithResult("$CHECKOUT_TAG_COMMAND/$latestTag", workingDir)
        val jsonComponents = JsonHelper.parseComponentsJson("$currentDirectory/$COMPONENTS_JSON_FILE_PATH")
        Components.init(jsonComponents)
        CommandLineRunner.runCommandWithResult("$CHECKOUT_COMMAND $currentBranch", workingDir)

        val allBintrayPackages = Bintray.getAllPackages()
        val sb = StringBuilder()

        // Check if deploy is available for the current component and bintray contains the component
        Components.value.forEach { component ->
            val name = component.name
            val stableVersion = component.baseVersion

            if (CommandLineRunner.isCommandSucceed("./gradlew :$name:checkIfDeployAvailable", workingDir)) {
                if (allBintrayPackages.contains(name)) {
                    println("checking latest bintray version for $name")
                    val bintrayVersion = Bintray.getArtifactLatestVersion(name).name
                    if (stableVersion != bintrayVersion) {
                        val errorMessage = "latest bintray version=$bintrayVersion for $name is not stable=$stableVersion"
                        sb.append("$errorMessage\n")
                        println("ERROR: $errorMessage")
                    } else {
                        println("latest bintray version=$bintrayVersion for $name is stable")
                    }
                } else {
                    println("CheckBintrayStableVersions warning: $name has stable version $stableVersion, " +
                            "but haven't deployed to bintray."
                    )
                }
            }
        }
        with(sb.toString()) {
            if (isNotEmpty()) {
                throw GradleException(this)
            }
        }
    }
}