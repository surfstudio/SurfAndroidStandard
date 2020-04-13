package ru.surfstudio.android.build.tasks.check_cross_feature_route.util

import org.gradle.api.logging.Logger
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureRouteWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassWrapper
import java.io.File

/**
 * Kotlin class file parser.
 *
 * Parses only routes that implements CrossFeatureRoute.
 * */
class KClassCrossFeatureRouteParser(logger: Logger) : KClassParser(logger) {

    override fun parse(file: File): KClassWrapper? {
        val parsingResult = super.parse(file) ?: return null

        val names = listOf(parsingResult.className, parsingResult.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val targetClassPath = parseTargetClassPath(parsingResult.classBody)
        val isCrossFeatureRoute = targetClassPath.isNotBlank()

        val result = when (isCrossFeatureRoute) {
            true -> {
                KClassCrossFeatureRouteWrapper(
                        parsingResult.packageName,
                        parsingResult.className,
                        parsingResult.baseClassPackageName,
                        parsingResult.baseClassName,
                        parsingResult.implementations,
                        parsingResult.classBody,
                        targetClassPath
                )
            }
            else -> null
        }

        val entityName = when {
            isActivity -> "ActivityRoute"
            isFragment -> "FragmentRoute"
            isDialog -> "DialogRoute"
            else -> "Route"
        }

        when (result) {
            null -> logger.info("$entityName ignored: $parsingResult")
            else -> logger.info("$entityName parsed: $parsingResult")
        }

        return result
    }

    private fun parseTargetClassPath(classBody: String): String {
        return classBody.substringSafe(findTargetClassPathRange(classBody))
    }

    private fun findTargetClassPathRange(target: String): IntRange {
        val funStart = target.indexAfter("fun targetClassPath()")
        val funSlice = target.substringSafe(funStart..target.lastIndex)
        val start = funStart + funSlice.indexAfter('"')
        val targetClassPathSlice = target.substringSafe(start..target.lastIndex)
        val end = start + targetClassPathSlice.indexBefore('"')
        return start..end
    }

}