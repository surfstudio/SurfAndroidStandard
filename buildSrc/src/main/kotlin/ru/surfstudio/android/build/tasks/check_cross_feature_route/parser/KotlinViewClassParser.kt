package ru.surfstudio.android.build.tasks.check_cross_feature_route.parser

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinClassWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinViewClassWrapper
import java.io.File

class KotlinViewClassParser(
        isDebugEnabled: Boolean
) : KotlinClassParser(isDebugEnabled) {

    private companion object {
        const val FORMAT_CLASS_ACTIVITY = "Activity"
        const val FORMAT_CROSS_FEATURE_FRAGMENT = "CrossFeatureFragment"
    }

    override fun parse(file: File): KotlinClassWrapper? {
        val result = super.parse(file) ?: return null
        val isImplementsCrossFeature = when {
            result.className.contains(FORMAT_CLASS_ACTIVITY) -> true
            else -> result.implementations.any { it.contains(FORMAT_CROSS_FEATURE_FRAGMENT) }
        }
        return KotlinViewClassWrapper(
                result.packageName,
                result.className,
                result.baseClassName,
                result.baseClassPackageName,
                result.implementations,
                result.classBody,
                isImplementsCrossFeature
        )
    }
}