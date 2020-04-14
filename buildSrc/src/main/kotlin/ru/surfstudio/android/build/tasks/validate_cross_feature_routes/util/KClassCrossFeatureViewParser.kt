package ru.surfstudio.android.build.tasks.validate_cross_feature_routes.util

import org.gradle.api.logging.Logger
import ru.surfstudio.android.build.tasks.validate_cross_feature_routes.data.KClassCrossFeatureViewWrapper
import ru.surfstudio.android.build.tasks.validate_cross_feature_routes.data.KClassWrapper
import java.io.File

/**
 * Kotlin class file parser.
 *
 * Parses all of views without any filtration because CrossFeatureRoute can be implemented
 * in base classуs and we must have possibility to reach those base classes.
 * */
class KClassCrossFeatureViewParser(logger: Logger) : KClassParser(logger) {

    override fun parse(file: File): KClassWrapper? {
        val parsingResult = super.parse(file) ?: return null

        val names = listOf(parsingResult.className, parsingResult.baseClassName)
        val isActivity: Boolean = names.any { it.contains("Activity") }
        val isFragment: Boolean = names.any { it.contains("Fragment") }
        val isDialog: Boolean = names.any { it.contains("Dialog") }
        val isImplementsCrossFeature: Boolean = when {
            isActivity -> true
            isFragment || isDialog -> parsingResult.implementations.contains("CrossFeatureFragment")
            else -> false
        }

        val result = KClassCrossFeatureViewWrapper(
                parsingResult.packageName,
                parsingResult.className,
                parsingResult.baseClassPackageName,
                parsingResult.baseClassName,
                parsingResult.implementations,
                parsingResult.classBody,
                isImplementsCrossFeature
        )

        val entityName = when {
            isActivity -> "ActivityView"
            isFragment -> "FragmentView"
            isDialog -> "DialogView"
            else -> "View"
        }
        logger.info("$entityName parsed: $parsingResult")

        return result
    }
}