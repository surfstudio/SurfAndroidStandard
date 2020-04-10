package ru.surfstudio.android.build.tasks.check_cross_feature_route.util

import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinCrossFeatureRouteClassWrapper
import ru.surfstudio.android.build.tasks.check_cross_feature_route.data.KotlinViewClassWrapper
import ru.surfstudio.android.build.utils.DefaultGradleLogger
import ru.surfstudio.android.build.utils.GradleLogger

/**
 * Верификатор пути к экрану.
 * */
class KotlinCrossFeatureRouteVerifier(
        private val views: List<KotlinViewClassWrapper>,
        isDebugEnabled: Boolean
) : GradleLogger by DefaultGradleLogger("KotlinRouteVerifier", isDebugEnabled) {

    fun verify(route: KotlinCrossFeatureRouteClassWrapper): Boolean {
        "${route.fullName} verifying".logd()
        val targetView = findViewClass(route.targetClassName, route.targetClassPackageName)
        if (targetView == null) {
            "Unable to find view (${route.targetClassPath}) for route ${route.fullName}".loge()
            return false
        }
        val isViewImplementsCrossFeature = checkIsCrossFeature(targetView)
        if (!isViewImplementsCrossFeature) {
            "${targetView.className} is not implements CrossFeatureFragment".loge()
            return false
        }
        "${route.fullName} verified".logd()
        return true
    }

    private fun checkIsCrossFeature(view: KotlinViewClassWrapper): Boolean {
        return when {
            view.isImplementsCrossFeature -> true
            else -> {
                val targetView = findViewClass(view.baseClassName, view.baseClassPackageName)
                targetView?.let(::checkIsCrossFeature) ?: false
            }
        }
    }

    private fun findViewClass(className: String, packageName: String): KotlinViewClassWrapper? {
        return views.find {
            it.className == className && it.packageName == packageName
        }
    }


}