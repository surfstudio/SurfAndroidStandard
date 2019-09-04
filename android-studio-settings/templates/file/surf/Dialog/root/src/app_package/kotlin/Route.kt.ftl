<#import "../../../../../Common/IntentAndBundleMacros.ftl" as IntentAndBundleMacros>

package ${packageName}

<#if needToGenerateParams>
import android.os.Bundle
</#if>
import ru.surfstudio.android.mvp.dialog.navigation.route.<#if needToGenerateParams>DialogWithParamsRoute<#else>DialogRoute</#if>

/**
 * Маршрут [${dialogClassName}].
 */
class ${dialogRouteClassName}<#if needToGenerateParams>(<#if routeParamName1!='' && routeParamClassName1!=''>
        val ${routeParamName1}: ${routeParamClassName1}</#if><#if routeParamName2!='' && routeParamClassName2!=''>,
        val ${routeParamName2}: ${routeParamClassName2}</#if><#if routeParamName3!='' && routeParamClassName3!=''>,
        val ${routeParamName3}: ${routeParamClassName3}</#if>
)</#if> : <#if needToGenerateParams>DialogWithParamsRoute<#else>DialogRoute</#if>() {

    override fun getFragmentClass() = ${dialogClassName}::class.java
    <#if needToGenerateParams>

    override fun prepareBundle() = Bundle().apply {
        <#if routeParamClassName1!='' && routeParamName1!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName1/>(EXTRA_FIRST, ${routeParamName1})
        </#if>
        <#if routeParamClassName2!='' && routeParamName2!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName2/>(EXTRA_SECOND, ${routeParamName2})
        </#if>
        <#if routeParamClassName3!='' && routeParamName3!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName3/>(EXTRA_THIRD, ${routeParamName3})
        </#if>
    }
    </#if>
}