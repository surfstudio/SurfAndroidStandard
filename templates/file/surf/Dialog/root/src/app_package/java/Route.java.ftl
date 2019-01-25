<#import "../macros/BundleMacros.ftl" as BundleMacros>

package ${packageName};
<#if needToGenerateParams>

import android.os.Bundle;
</#if>

import ru.surfstudio.android.mvp.dialog.navigation.route.<#if needToGenerateParams>DialogWithParamsRoute<#else>DialogRoute</#if>;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment;

/**
 * Маршрут [${dialogClassName}].
 */
public class ${dialogRouteClassName} extends <#if needToGenerateParams>DialogWithParamsRoute<#else>DialogRoute</#if> {
    <#if needToGenerateParams>

    <#if routeParamClassName1!='' && routeParamName1!=''>
    private ${routeParamClassName1} ${routeParamName1};
    </#if>
    <#if routeParamClassName2!='' && routeParamName2!=''>
    private ${routeParamClassName2} ${routeParamName2};
    </#if>
    <#if routeParamClassName3!='' && routeParamName3!=''>
    private ${routeParamClassName3} ${routeParamName3};
    </#if>

    public ${dialogRouteClassName}(<#if routeParamName1!='' && routeParamClassName1!=''>
            ${routeParamClassName1} ${routeParamName1}</#if><#if routeParamName2!='' && routeParamClassName2!=''>,
            ${routeParamClassName2} ${routeParamName2}</#if><#if routeParamName3!='' && routeParamClassName3!=''>,
            ${routeParamClassName3} ${routeParamName3}</#if>
    ) {
        <#if routeParamClassName1!='' && routeParamName1!=''>
        this.${routeParamName1} = ${routeParamName1};
        </#if>
        <#if routeParamClassName2!='' && routeParamName2!=''>
        this.${routeParamName2} = ${routeParamName2};
        </#if>
        <#if routeParamClassName3!='' && routeParamName3!=''>
        this.${routeParamName3} = ${routeParamName3};
        </#if>
    }
    </#if>

    @Override
    protected Class<? extends CoreSimpleDialogFragment> getFragmentClass() {
        return ${dialogClassName}.class;
    }
    <#if needToGenerateParams>

    @Override
    public Bundle prepareBundle() {
        Bundle args = new Bundle();
        <#if routeParamName1!='' && routeParamClassName1!=''>
            args.<@BundleMacros.generatePutToBundleMethod routeParamClassName1/>(EXTRA_FIRST, ${routeParamName1});
        </#if>
        <#if routeParamName2!='' && routeParamClassName2!=''>
            args.<@BundleMacros.generatePutToBundleMethod routeParamClassName2/>(EXTRA_SECOND, ${routeParamName2});
        </#if>
        <#if routeParamName3!='' && routeParamClassName3!=''>
            args.<@BundleMacros.generatePutToBundleMethod routeParamClassName3/>(EXTRA_THIRD, ${routeParamName3});
        </#if>
        return args;
    }
    </#if>
}
