package ru.surfstudio.android.build.tasks.check_cross_feature_route.data

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