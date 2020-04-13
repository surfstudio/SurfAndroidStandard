package ru.surfstudio.android.build.tasks.check_cross_feature_route

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureRouteWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureViewWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.util.KClassCrossFeatureRouteParser
import ru.surfstudio.android.build.tasks.check_cross_feature_route.util.KClassCrossFeatureRouteValidator
import ru.surfstudio.android.build.tasks.check_cross_feature_route.util.KClassCrossFeatureViewParser
import java.io.File

/**
 * Task validate's all of CrossFeatureRoute's in project.
 * */
open class ValidateCrossFeatureRoutesTask : DefaultTask() {

    /**
     * Result of the directory scanning.
     *
     * @param routes list of routes found in this directory and it's sub-directories.
     * @param views list of views found in this directory and it's sub-directories.
     * */
    private data class DirectoryScanResult(val routes: List<File>, val views: List<File>)

    /**
     * Flag: true -> skip validation of routes and return immediately.
     * */
    var shouldSkipValidation: Boolean = false

    /**
     * List of file names which gonna be ignored on project scan.
     * */
    var ignoredFileNames: List<String> = listOf("build")

    /**
     * Validate all of CrossFeatureRoute's in project.
     * */
    @TaskAction
    fun validate() {
        if (shouldSkipValidation) {
            logger.warn("Validation of CrossFeatureRoutes disabled.")
            return
        }

        logger.lifecycle("Validating CrossFeatureRoute's...")
        val directoryScanResult = scanDirectory(project.rootDir)
        val routeParser = KClassCrossFeatureRouteParser(logger)
        val viewParser = KClassCrossFeatureViewParser(logger)

        val crossFeatureRouteWrappers = directoryScanResult.routes
                .mapNotNull { routeParser.parse(it) }
                .filterIsInstance<KClassCrossFeatureRouteWrapper>()

        val crossFeatureViewWrappers = directoryScanResult.views
                .mapNotNull { viewParser.parse(it) }
                .filterIsInstance<KClassCrossFeatureViewWrapper>()

        logger.info("Count of CrossFeatureRoutes to validate: ${crossFeatureRouteWrappers.size}")
        logger.info("Count of found views, used in CrossFeatureRoute validation: ${crossFeatureViewWrappers.size}")

        val routeValidator = KClassCrossFeatureRouteValidator(logger, crossFeatureViewWrappers)
        crossFeatureRouteWrappers.forEach { routeValidator.validate(it) }
        logger.lifecycle("All CrossFeatureRoute's validated.")
    }

    /**
     * Scan directory and fetch all of sub-directories and files from it.
     * */
    private fun scanDirectory(dir: File): DirectoryScanResult {
        val filteredFiles = dir.listFiles()
                ?.filter { !ignoredFileNames.contains(it.name) } ?: arrayListOf()

        val kotlinFiles = filteredFiles.filter { it.extension == "kt" }
        val directories = filteredFiles.filter { it.isDirectory }

        val directoryRouteFiles = kotlinFiles.filter { it.name.contains("Route") }
        val directoryViewFiles = kotlinFiles.filter { it.name.contains("View") }

        val scanResults = mutableListOf<DirectoryScanResult>().apply {
            add(DirectoryScanResult(directoryRouteFiles, directoryViewFiles))
        }

        directories.forEach { directory ->
            scanDirectory(directory).also { scanResults.add(it) }
        }

        val allRouteFiles = scanResults.flatMap { it.routes }
        val allViewFiles = scanResults.flatMap { it.views }
        return DirectoryScanResult(allRouteFiles, allViewFiles)
    }
}