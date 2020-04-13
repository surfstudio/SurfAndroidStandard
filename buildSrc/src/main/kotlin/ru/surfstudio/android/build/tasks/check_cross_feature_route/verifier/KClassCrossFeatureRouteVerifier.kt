package ru.surfstudio.android.build.tasks.check_cross_feature_route.verifier

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureRouteWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KClassCrossFeatureViewWrapper
import ru.surfstudio.android.build.utils.DefaultGradleLogger
import ru.surfstudio.android.build.utils.GradleLogger
import ru.surfstudio.android.build.utils.ThrowableGradleLogger

/**
 * Верификатор пути к экрану.
 *
 * Производит проверку CrossFeatureRoute:
 * 1. CrossFeatureRoute ссылается на существующую View;
 * 2. View является Activity или имплементирует CrossFeatureFragment.
 *
 * Список View, по которым происходит проверка передается в конструкторе.
 * */
class KClassCrossFeatureRouteVerifier(
        private val views: List<KClassCrossFeatureViewWrapper>
) : GradleLogger by ThrowableGradleLogger("KClassCrossFeatureRouteVerifier") {

    fun verify(route: KClassCrossFeatureRouteWrapper): Boolean {
        val routeTarget = findViewClass(route.targetClassName, route.targetClassPackageName)
        if (routeTarget == null) {
            "Unable to find view for $route".loge()
            return false
        }

        val isViewImplementsCrossFeature = checkIsCrossFeature(routeTarget)
        if (!isViewImplementsCrossFeature) {
            "$routeTarget is not implements CrossFeatureFragment for $route".loge()
            return false
        }

        "Verified: $route".logi()
        return true
    }

    private fun checkIsCrossFeature(view: KClassCrossFeatureViewWrapper): Boolean {
        return when {
            view.isImplementsCrossFeature -> true
            else -> {
                val routeTargetParent = findViewClass(view.baseClassName, view.baseClassPackageName)
                routeTargetParent?.let(::checkIsCrossFeature) ?: false
            }
        }
    }

    private fun findViewClass(className: String, packageName: String): KClassCrossFeatureViewWrapper? {
        return views.find {
            it.className == className && it.packageName == packageName
        }
    }
}