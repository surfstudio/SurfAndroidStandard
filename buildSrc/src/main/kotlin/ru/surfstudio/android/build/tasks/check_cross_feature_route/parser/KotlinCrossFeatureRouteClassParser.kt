package ru.surfstudio.android.build.tasks.check_cross_feature_route.parser

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinClassWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinCrossFeatureRouteClassWrapper
import java.io.File

/**
 * Парсер Route-файлов.
 * */
class KotlinCrossFeatureRouteClassParser(isDebugEnabled: Boolean = false) :
        KotlinClassParser(isDebugEnabled) {

    private companion object {
        const val FORMAT_FUN_TARGET_CLASS_PATH = "fun targetClassPath()"
        const val FORMAT_TARGET_CLASS_PATH_START = '"'
        const val FORMAT_TARGET_CLASS_PATH_END = '"'
    }

    override var tag = "KotlinCrossFeatureRouteClassParser"

    override fun parse(file: File): KotlinClassWrapper? {
        val result = super.parse(file) ?: return null
        val targetClassPath = parseTargetClassPath(result.classBody)
        return if (targetClassPath.isBlank()) null
        else KotlinCrossFeatureRouteClassWrapper(
                result.packageName,
                result.className,
                result.baseClassName,
                result.baseClassPackageName,
                result.implementations,
                result.classBody,
                targetClassPath
        )
    }

    private fun parseTargetClassPath(target: String): String {
        return target.substringSafe(findTargetClassPathRange(target))
    }

    private fun findTargetClassPathRange(target: String): IntRange {
        val funStart = target.indexAfter(FORMAT_FUN_TARGET_CLASS_PATH)
        val funSlice = target.substringSafe(funStart..target.lastIndex)
        val start = funStart + funSlice.indexAfter(FORMAT_TARGET_CLASS_PATH_START)
        val targetClassPathSlice = target.substringSafe(start..target.lastIndex)
        val end = start + targetClassPathSlice.indexBefore(FORMAT_TARGET_CLASS_PATH_END)
        return start..end
    }

}