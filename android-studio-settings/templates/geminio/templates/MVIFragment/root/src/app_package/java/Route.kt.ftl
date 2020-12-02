<#assign screenType ="<#if fragment>Fragment<#else>Activity</#if>>
<#assign routeParentClassName ="${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route">

package ${packageName}

<#if useNewNavigation>
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
<#else>
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.FragmentCrossFeatureRoute
</#if>

/**
 * Роут экрана ${viewClassName}.
 */
<#if useNewNavigation>
class ${routeClassName} : FragmentRoute() {

    override fun getScreenClassPath(): String = "${packageName}.${viewClassName}"
}
<#else>
class ${routeClassName} : FragmentCrossFeatureRoute() {

    override fun targetClassPath() = "${packageName}.${viewClassName}"
}
</#if>