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
        val targetView = views.find {
            it.className == route.targetClassName && it.packageName == route.targetPackageName
        }

        return when {
            targetView == null -> {
                "Unable to find targetClass (${route.targetClassPath}) for route ${route.className}".loge()
                return false
            }

            !targetView.isImplementsCrossFeature -> {
                "${route.targetClassPath} is not implements CrossFeatureFragment".loge()
                return false
            }

            else -> true
        }
    }

}