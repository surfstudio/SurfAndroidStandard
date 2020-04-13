package ru.surfstudio.android.build.tasks.check_cross_feature_route.util

import org.gradle.api.logging.Logger
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureRouteWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureViewWrapper

/**
 * Validate's CrossFeatureRoute by following steps:
 * 1. Check is target view exist;
 * 2. Check is target view Activity or implements CrossFeatureFragment;
 *
 * @param logger entity to log message in to.
 * @param views list of views, used to validate CrossFeatureRoute.
 * */
class KClassCrossFeatureRouteValidator(
        private val logger: Logger,
        private val views: List<KClassCrossFeatureViewWrapper>
) {

    /**
     * Validate CrossFeatureRoute.
     * */
    fun validate(route: KClassCrossFeatureRouteWrapper) {
        val routeTargetView = findViewClass(route.targetClassPackageName, route.targetClassName)
                ?: error("Unable to find view for $route")

        val isViewImplementsCrossFeature = checkIsImplementsCrossFeature(routeTargetView)
        if (!isViewImplementsCrossFeature) {
            error("$routeTargetView is not implements CrossFeatureFragment for $route")
        }

        logger.info("Verified: $route")
    }

    private fun checkIsImplementsCrossFeature(view: KClassCrossFeatureViewWrapper): Boolean {
        return when {
            view.isImplementsCrossFeature -> true
            else -> {
                val routeTargetParent = findViewClass(view.baseClassName, view.baseClassPackageName)
                routeTargetParent?.let(::checkIsImplementsCrossFeature) ?: false
            }
        }
    }

    private fun findViewClass(packageName: String, className: String): KClassCrossFeatureViewWrapper? {
        return views.find { it.className == className && it.packageName == packageName }
    }
}