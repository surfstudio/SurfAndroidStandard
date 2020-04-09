package ru.surfstudio.android.build.tasks.check_cross_feature_route

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.utils.EMPTY_STRING
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
open class CheckScreenRoutesTask : DefaultTask() {

    private val charDot = '.'
    private val charQuote = '"'
    private val charColon = ':'
    private val charBraceOpen = '{'
    private val charBraceClose = '}'
    private val charSlash = '\\'

    private val scanExcludedNames = listOf("build")

    private val keywordClass = "class "
    private val funTargetClassPath = "fun targetClassPath()"

    private val isDebugEnabled = true

    @TaskAction
    fun check() {
        "Check screen routes started.".log()
        val timeTaken = measureTime {
            "Scanning project...".log()
            val scanResult = scanDirectory(project.projectDir)
            "\nList of found routes:".log()
            scanResult.routes.forEach { it.absolutePath.log() }
            "\nList of found activities:".log()
            scanResult.activities.forEach { it.absolutePath.log() }
            "\nList of found fragments:".log()
            scanResult.fragments.forEach { it.absolutePath.log() }
            "\nScanning for CrossFeatureRoutes...".log()
            val crossFeatureRoutes = scanResult.routes.mapToCrossFeatureRoute()
            "\nList of found CrossFeatureRoutes:".log()
            crossFeatureRoutes.forEach { it.toString().log() }
            "\nValidating CrossFeatureRoutes targetClassPath...".log()
            crossFeatureRoutes.forEach { validateTargetClassPath(scanResult, it) }
        }
        "\nCheck screen routes finished. Time taken: ${timeTaken.inSeconds}"
    }

    private fun scanDirectory(dir: File): ScanResult {
        val filteredFiles = dir.listFiles()?.filter { !scanExcludedNames.contains(it.name) }
                ?: arrayListOf()

        val kotlinFiles = filteredFiles.filter { it.extension == "kt" }
        val directories = filteredFiles.filter { it.isDirectory }

        val innerRouteFiles = kotlinFiles.filter { it.name.contains("Route") }
        val innerActivityFiles = kotlinFiles.filter { it.name.contains("ActivityView") }
        val innerFragmentFiles = kotlinFiles.filter { it.name.contains("FragmentView") }
        val innerDialogFiles = kotlinFiles.filter { it.name.contains("DialogView") }

        val scanResults = mutableListOf<ScanResult>().apply {
            add(ScanResult(innerRouteFiles, innerActivityFiles, innerFragmentFiles, innerDialogFiles))
        }

        directories.forEach { directory ->
            scanDirectory(directory).also { scanResults.add(it) }
        }

        val routeFiles = scanResults.flatMap { it.routes }
        val activityFiles = scanResults.flatMap { it.activities }
        val fragmentFiles = scanResults.flatMap { it.fragments }
        val dialogFiles = scanResults.flatMap { it.dialogs }
        return ScanResult(routeFiles, activityFiles, fragmentFiles, dialogFiles)
    }

    private fun validateTargetClassPath(scanResult: ScanResult, route: CrossFeatureRoute) {
        val candidates = when {
            route.isActivity -> scanResult.activities.filter { it.name.contains(route.targetClassName) }
            route.isFragment -> scanResult.fragments.filter { it.name.contains(route.targetClassName) }
            route.isDialog -> scanResult.dialogs.filter { it.name.contains(route.targetClassName) }
            else -> error("Unknown route type: ${route.name}")
        }

        candidates.forEach { candidate ->
            if (candidate.absolutePath.contains(route.targetClassSystemPath)) {
                "${route.name} for ${route.targetClassName} is valid".log()
                return
            }
        }

        throw error("${route.name} is not valid")
    }

    private fun List<File>.mapToCrossFeatureRoute(): List<CrossFeatureRoute> {
        return mapNotNull { routeFile ->
            val crossFeatureRoute = CrossFeatureRoute(routeFile)
            if (crossFeatureRoute.isValid) crossFeatureRoute else null
        }
    }

    private fun String.log() = if (isDebugEnabled) println(this) else Unit

    private data class ScanResult(
            val routes: List<File>,
            val activities: List<File>,
            val fragments: List<File>,
            val dialogs: List<File>
    )

    private inner class CrossFeatureRoute(private val file: File) {

        val name get() = file.nameWithoutExtension ?: EMPTY_STRING

        private val fileContent = file.readText()
        val baseClassName = findBaseClassName(fileContent)
        val targetClassPath = findTargetClassPath(fileContent)
        val targetClassSystemPath = parseTargetClassSystemPath(targetClassPath)
        val targetClassName = parseTargetClassName(targetClassPath)
        val isActivity = fileContent.contains("ActivityRoute")
        val isFragment = fileContent.contains("FragmentRoute")
        val isDialog = fileContent.contains("DialogRoute")
        val isValid = targetClassPath.isNotBlank()

        override fun toString(): String {
            return "${file.nameWithoutExtension} : $baseClassName (targetClassPath = $targetClassPath)"
        }

        private fun parseTargetClassName(targetClassPath: String): String {
            return if (!targetClassPath.contains(charDot)) EMPTY_STRING
            else targetClassPath.substringAfterLast(charDot)
        }

        private fun parseTargetClassSystemPath(targetClassPath: String): String {
            return targetClassPath.replace(charDot, charSlash)
        }

        private fun findTargetClassPath(fileContent: String): String {
            var result = EMPTY_STRING
            try {
                val funText = fileContent.indexOf(funTargetClassPath).let { startIndex ->
                    if (startIndex < 0) return result
                    else fileContent.substring(startIndex)
                }

                val funBodyStart = funText.indexOf(charBraceOpen) + 1
                val funBodyEnd = funText.indexOf(charBraceClose) - 1
                val funBodyText = (funBodyStart..funBodyEnd).let { range ->
                    if (-1 in range) error("can't find targetClassPath()-method body")
                    else funText.substring(range)
                }

                val targetClassPathStart = funBodyText.indexOf(charQuote) + 1
                val targetClassPathEnd = funBodyText.lastIndexOf(charQuote) - 1
                result = (targetClassPathStart..targetClassPathEnd).let { range ->
                    if (-1 in range) error("can't parse targetClassPath()-method body")
                    else funBodyText.substring(range)
                }
            } catch (error: Throwable) {
                handleError(error)
            }
            return result
        }

        private fun findBaseClassName(fileContent: String): String {
            var result = EMPTY_STRING
            try {
                val classText = fileContent.indexOf(keywordClass).let { startIndex ->
                    return@let if (startIndex < 0) return result
                    else fileContent.substring(startIndex)
                }

                val implsEnd = classText.indexOf(charBraceOpen).let { endIndex ->
                    return@let if (endIndex < 0) classText.lastIndex
                    else endIndex
                }

                val implsStart = run {
                    (implsEnd downTo 0).forEach { index ->
                        if (classText[index] == charColon) return@run index
                    }
                    return@run -1
                }

                val implsText = (implsStart..implsEnd).let { range ->
                    if (-1 in range) return result
                    else classText.substring(range)
                }

                val baseStart = implsText.indexOfFirst { it.isLetter() }
                val baseEnd = run {
                    (baseStart..implsText.lastIndex).forEach { index ->
                        if (!implsText[index].isLetter()) {
                            return@run index - 1
                        }
                    }
                    return@run -1
                }

                result = (baseStart..baseEnd).let { range ->
                    if (-1 in range) return result
                    else implsText.substring(range)
                }
            } catch (error: Throwable) {
                handleError(error)
            }
            return result
        }

        private fun handleError(error: Throwable) {
            "ERROR ${file.nameWithoutExtension}: ${error.message}".log()
        }
    }
}