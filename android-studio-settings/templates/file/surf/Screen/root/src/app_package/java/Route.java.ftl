<#import "../../../../../Common/IntentAndBundleMacros.ftl" as IntentAndBundleMacros>

package ${packageName};

import android.content.Context;
import android.content.Intent;
<#if screenType=='fragment'>
import android.os.Bundle;
</#if>
<#if crossFeature>
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.${routeParentClassName}
<#else>
import ru.surfstudio.android.core.ui.navigation.${screenType}.route.${routeParentClassName}
</#if>
<#if screenType=='fragment'>
import androidx.fragment.app.Fragment;
</#if>

/**
 * Маршрут [${viewClassName}].
 */
public class ${routeClassName} extends ${routeParentClassName} {
    <#if needToGenerateParams>

    <#if routeParamClassName1!='' && routeParamName1!=''>
    private final ${routeParamClassName1} ${routeParamName1};
    </#if>
    <#if routeParamClassName2!='' && routeParamName2!=''>
    private final ${routeParamClassName2} ${routeParamName2};
    </#if>
    <#if routeParamClassName3!='' && routeParamName3!=''>
    private final ${routeParamClassName3} ${routeParamName3};
    </#if>

    public ${routeClassName}(<#if routeParamName1!='' && routeParamClassName1!=''>
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

    <#if screenType=='activity'>
    ${routeClassName}(Intent intent) {
    <#if routeParamName1!='' && routeParamClassName1!=''>
    ${routeParamName1} = intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName1/>(EXTRA_FIRST<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName1/>);
    </#if>
    <#if routeParamName2!='' && routeParamClassName2!=''>
    ${routeParamName2} = intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName2/>(EXTRA_SECOND<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName2/>);
    </#if>
    <#if routeParamName3!='' && routeParamClassName3!=''>
    ${routeParamName1} = intent.<@IntentAndBundleMacros.generateGetFromIntentMethod routeParamClassName3/>(EXTRA_THIRD<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName3/>);
    </#if>
    }
    <#else>
    ${routeClassName}(Bundle args) {
        <#if routeParamName1!='' && routeParamClassName1!=''>
        ${routeParamName1} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClass1/>(EXTRA_FIRST<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName1/>);
        </#if>
        <#if routeParamName2!='' && routeParamClassName2!=''>
        ${routeParamName2} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClass2/>(EXTRA_SECOND<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName2/>);
        </#if>
        <#if routeParamName3!='' && routeParamClassName3!=''>
        ${routeParamName3} = args.<@IntentAndBundleMacros.generateGetFromBundleMethod routeParamClass3/>(EXTRA_THIRD<@IntentAndBundleMacros.generateDefaultValueIfNeeded routeParamName3/>);
        </#if>
    }
    </#if>
    </#if>

    <#if screenType='activity'>
    @Override
    public Intent prepareIntent(Context context) {
        Intent intent = new Intent(context, ${viewClassName}.class);
        <#if routeParamClassName1!='' && routeParamName1!=''>
        intent.putExtra(EXTRA_FIRST, ${routeParamName1});
        </#if>
        <#if routeParamClassName2!='' && routeParamName2!=''>
        intent.putExtra(EXTRA_SECOND, ${routeParamName2});
        </#if>
        <#if routeParamClassName3!='' && routeParamName3!=''>
        intent.putExtra(EXTRA_THIRD, ${routeParamName3});
        </#if>
        return intent;
    }
    <#else>
    <#if !crossFeature>
    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return ${viewClassName}.class;
    }
    </#if>

    @Override
    public Bundle prepareBundle() {
        Bundle args = new Bundle();
        <#if routeParamName1!='' && routeParamClassName1!=''>
            args.<@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName1/>(EXTRA_FIRST, ${routeParamName1});
        </#if>
        <#if routeParamName2!='' && routeParamClassName2!=''>
            args.<@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName2/>(EXTRA_SECOND, ${routeParamName2});
        </#if>
        <#if routeParamName3!='' && routeParamClassName3!=''>
            args.<@IntentAndBundleMacros.generatePutToBundleMethod routeParamClassName3/>(EXTRA_THIRD, ${routeParamName3});
        </#if>
        return args;
    }
    </#if>


    <#if crossFeature>
    @Override
    public String targetClassPath() {
        return "${packageName}.${viewClassName}";
    }
    </#if>
}
