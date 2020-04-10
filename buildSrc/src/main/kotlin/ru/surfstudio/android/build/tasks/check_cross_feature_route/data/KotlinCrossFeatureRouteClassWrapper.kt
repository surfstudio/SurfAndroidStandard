package ru.surfstudio.android.build.tasks.check_cross_feature_route.data

class KotlinCrossFeatureRouteClassWrapper(
        packageName: String,
        className: String,
        baseClassName: String,
        baseClassPackageName: String,
        implementations: List<String>,
        classBody: String,
        val targetClassPath: String
) : KotlinClassWrapper(
        packageName,
        className,
        baseClassName,
        baseClassPackageName,
        implementations,
        classBody
) {

    val targetClassName: String = targetClassPath.substringAfterLast('.')
    val targetClassPackageName: String = targetClassPath.substringBeforeLast('.')
}