package ru.surfstudio.android.build.tasks.check_cross_feature_route.data

class KotlinViewClassWrapper(
        packageName: String,
        className: String,
        baseClassName: String,
        baseClassPackageName: String,
        implementations: List<String>,
        classBody: String,
        val isImplementsCrossFeature: Boolean
) : KotlinClassWrapper(
        packageName,
        className,
        baseClassName,
        baseClassPackageName,
        implementations,
        classBody
)