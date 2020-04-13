package ru.surfstudio.android.build.tasks.check_cross_feature_route

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureRouteWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureViewWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.parser.KClassCrossFeatureRouteParser
import ru.surfstudio.android.build.tasks.check_cross_feature_route.parser.KClassCrossFeatureViewParser
import ru.surfstudio.android.build.tasks.check_cross_feature_route.verifier.KClassCrossFeatureRouteVerifier
import ru.surfstudio.android.build.utils.DefaultGradleLogger
import ru.surfstudio.android.build.utils.GradleLogger
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
open class CheckCrossFeatureRouteTask :
        DefaultTask(),
        GradleLogger by DefaultGradleLogger("CheckCrossFeatureRouteTask") {

    private data class ScanResult(val routes: List<File>, val views: List<File>)

    private val scanExcludedNames = listOf("build")

    // TODO добавить gradle-task параметр
    private val isCheckRequired = true

    @TaskAction
    fun check() {
        if (!isCheckRequired) {
            "Task execution disabled.".logi()
            return
        }

        val timeTaken = measureTime {
            "Scanning project...".logi()
            val scanResult = scanDirectory(project.rootDir)
            val routeParser = KClassCrossFeatureRouteParser()
            val viewParser = KClassCrossFeatureViewParser()

            val crossFeatureRouteWrappers = scanResult.routes
                    .mapNotNull { routeParser.parse(it) }
                    .filterIsInstance<KClassCrossFeatureRouteWrapper>()

            val viewWrappers = scanResult.views
                    .mapNotNull { viewParser.parse(it) }
                    .filterIsInstance<KClassCrossFeatureViewWrapper>()

            if (isDebugEnabled) {
                "Found CrossFeatureRoutes: ${crossFeatureRouteWrappers.size}".logd()
                "Found routes: ${scanResult.routes.size}".logd()
                "Found views: ${scanResult.views.size}".logd()
                "List of CrossFeatureRoutes:".logd()
                crossFeatureRouteWrappers.forEachIndexed { index, item -> "${index + 1}. ${item.className}".logd() }
            }

            "Project scanning done. Verifying routes...".logi()
            val crossFeatureRouteVerifier = KClassCrossFeatureRouteVerifier(viewWrappers)
            crossFeatureRouteWrappers.forEach { crossFeatureRouteVerifier.verify(it) }
        }
        "Task executed. Time taken: ${timeTaken.inSeconds}".logi()
    }

    private fun scanDirectory(dir: File): ScanResult {
        val filteredFiles = dir.listFiles()?.filter { !scanExcludedNames.contains(it.name) }
                ?: arrayListOf()

        val kotlinFiles = filteredFiles.filter { it.extension == "kt" }
        val directories = filteredFiles.filter { it.isDirectory }

        val innerRouteFiles = kotlinFiles.filter { it.name.contains("Route") }
        val innerViews = kotlinFiles.filter { it.name.contains("View") }

        val scanResults = mutableListOf<ScanResult>().apply {
            add(ScanResult(innerRouteFiles, innerViews))
        }

        directories.forEach { directory ->
            scanDirectory(directory).also { scanResults.add(it) }
        }

        val routeFiles = scanResults.flatMap { it.routes }
        val viewFiles = scanResults.flatMap { it.views }
        return ScanResult(routeFiles, viewFiles)
    }

    // TODO invalid cross feature exception?
    /*private fun validateCrossFeatureRoute(scanResult: ScanResult, route: CrossFeatureRoute) {
        val candidates = when {
            route.isActivity -> scanResult.activities.filter { it.name.contains(route.targetClassName) }
            route.isFragment -> scanResult.fragments.filter { it.name.contains(route.targetClassName) }
            route.isDialog -> scanResult.dialogs.filter { it.name.contains(route.targetClassName) }
            else -> error("Unknown route type: ${route.name}")
        }

        candidates.forEach { candidate ->
            if (candidate.absolutePath.contains(route.targetClassSystemPath)) {
                //"${route.name} for ${route.targetClassName} is valid".log()

                return
            }
        }

        throw error("${route.name} is not valid")
    }*/
}