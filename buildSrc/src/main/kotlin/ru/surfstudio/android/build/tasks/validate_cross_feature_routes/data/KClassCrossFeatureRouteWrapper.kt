package ru.surfstudio.android.build.tasks.validate_cross_feature_routes.data

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
class KClassCrossFeatureRouteWrapper(
        packageName: String,
        className: String,
        baseClassPackageName: String,
        baseClassName: String,
        implementations: List<String>,
        classBody: String,
        val targetClassPath: String
) : KClassWrapper(
        packageName,
        className,
        baseClassPackageName,
        baseClassName,
        implementations,
        classBody
) {
    val targetClassName: String = targetClassPath.substringAfterLast('.')
    val targetClassPackageName: String = targetClassPath.substringBeforeLast('.')

    override fun toString(): String {
        return "${super.toString()} (target = $targetClassPath)"
    }
}