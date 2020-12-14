<#if (fragment)>
    <#assign screenType = "Fragment">
<#else>
    <#assign screenType = "Activity">
</#if>
<#assign emptyClassName = "Unit">
<#import "IntentAndBundleMacros.ftl" as IntentAndBundleMacros>

package ${packageName}

import android.content.Context
import android.content.Intent
import android.os.Bundle
<#if crossFeature>
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route
<#else>
import ru.surfstudio.android.core.ui.navigation.${screenType}.route.${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route
</#if>
<#if (fragment)>
import androidx.fragment.app.Fragment
</#if>

/**
 * Маршрут [${viewClassName}].
 */
class ${routeClassName}<#if needToGenerateParams>(<#if routeParamName1!='' && routeParamClassName1!='emptyClassName'>
        val ${routeParamName1}: ${routeParamClassName1}</#if><#if routeParamName2!='' && routeParamClassName2!=''>,
        val ${routeParamName2}: ${routeParamClassName2}</#if><#if routeParamName3!='' && routeParamClassName3!=''>,
        val ${routeParamName3}: ${routeParamClassName3}</#if>
)</#if> : ${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route<#if needToGenerateResult><${resultClass}>,</#if>() {

    <#if (activity)>
    <#if needToGenerateParams>
    constructor(intent: Intent) : this(<#if routeParamName1!='' && routeParamClassName1!=''>
            intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName1/>(EXTRA_FIRST<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName1/>)</#if><#if routeParamName2!='' && routeParamClassName2!=''>,
            intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName2/>(EXTRA_SECOND<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName2/>)</#if><#if routeParamName3!='' && routeParamClassName3!=''>,
            intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName3/>(EXTRA_THIRD<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName3/>)</#if>
    )

    </#if>
    override fun prepareIntent(context: Context) = Intent(context, ${viewClassName}::class.java).apply {
        <#if routeParamClassName1!='' && routeParamName1!=''>
        intent.putExtra(EXTRA_FIRST, ${routeParamName1})
        </#if>
        <#if routeParamClassName2!='' && routeParamName2!=''>
        intent.putExtra(EXTRA_SECOND, ${routeParamName2})
        </#if>
        <#if routeParamClassName3!='' && routeParamName3!=''>
        intent.putExtra(EXTRA_THIRD, ${routeParamName3})
        </#if>
    }
    <#else>
    <#if needToGenerateParams>
    constructor(args: Bundle) : this (<#if routeParamName1?has_content && routeParamClassName1?has_content>
            ${routeParamName1} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClassName1/>(EXTRA_FIRST<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName1/>)</#if><#if routeParamName2?has_content && routeParamClassName2?has_content>,
            ${routeParamName2} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClassName2/>(EXTRA_SECOND<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName2/>)</#if><#if routeParamName3?has_content && routeParamClassName3?has_content>,
            ${routeParamName3} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClassName3/>(EXTRA_THIRD<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamClassName3/>)</#if>
    )

    </#if>
    <#if !crossFeature>
    override fun getFragmentClass() = ${viewClassName}::class.java
    </#if>
    <#if needToGenerateParams>

    override fun prepareBundle() = Bundle()<#if needToGenerateParams>.apply {
        <#if routeParamClassName1!='' && routeParamName1!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName1/>(EXTRA_FIRST, ${routeParamName1})
        </#if>
        <#if routeParamClassName2!='' && routeParamName2!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName2/>(EXTRA_SECOND, ${routeParamName2})
        </#if>
        <#if routeParamClassName3!='' && routeParamName3!=''>
        <@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName3/>(EXTRA_THIRD, ${routeParamName3})
        </#if>
    }</#if>
    </#if>
    </#if>
    <#if crossFeature>

    override fun targetClassPath() = "${packageName}.${viewClassName}"
    </#if>
}