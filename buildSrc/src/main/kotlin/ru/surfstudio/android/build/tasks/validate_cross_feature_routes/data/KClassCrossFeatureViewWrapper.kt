package ru.surfstudio.android.build.tasks.validate_cross_feature_routes.data

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
class KClassCrossFeatureViewWrapper(
        packageName: String,
        className: String,
        baseClassPackageName: String,
        baseClassName: String,
        implementations: List<String>,
        classBody: String,
        val isImplementsCrossFeature: Boolean
) : KClassWrapper(
        packageName,
        className,
        baseClassPackageName,
        baseClassName,
        implementations,
        classBody
)