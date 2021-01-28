package ${packageName}

<#if useNewNavigation>
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
<#else>
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute
</#if>

/**
 * Роут экрана ${viewClassName}.
 */
<#if useNewNavigation>
class ${routeClassName} : ActivityRoute() {

    override fun getScreenClassPath(): String = "${packageName}.${viewClassName}"
}
<#else>
class ${routeClassName} : ActivityCrossFeatureRoute() {

    override fun targetClassPath() = "${packageName}.${viewClassName}"
}
</#if>