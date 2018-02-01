<#import "macros/select_type_route_macros.ftl" as superClass>
<#import "macros/select_intent_read_method_macros.ftl" as intentRead>
<#import "macros/select_bundle_read_method_macros.ftl" as bundleRead>
<#import "macros/select_bundle_write_method_macros.ftl" as bundleWrite>
package ${packageName};

<#if screenType=='fragment'>
import android.support.v4.app.Fragment
</#if>

@Data
public class ${className}${screenTypeCapitalized}Route extends <@superClass.selectTypeRoute /> {

    <#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>
        <#if (routeParamType1!='' && routeParam1!='') || (routeParamType2!='' && routeParam2!='') || (routeParamType3!='' && routeParam3!='')>
            <#if routeParamType1!='' && routeParam1!=''>
            private ${routeParamType1} ${routeParam1};
            </#if>
            <#if routeParamType2!='' && routeParam2!=''>
            private ${routeParamType2} ${routeParam2};
            </#if>
            <#if routeParamType3!='' && routeParam3!=''>
            private ${routeParamType3} ${routeParam3};
            </#if>

            public ${className}${screenTypeCapitalized}Route(<#if routeParamType1!='' && routeParam1!=''>${routeParamType1} ${routeParam1}</#if><#if routeParamType2!='' && routeParam2!=''>, ${routeParamType2} ${routeParam2}</#if><#if routeParamType3!='' && routeParam3!=''>, ${routeParamType3} ${routeParam3}</#if>) {
                <#if routeParamType1!='' && routeParam1!=''>
                    this.${routeParam1} = ${routeParam1};
                </#if>
                <#if routeParamType2!='' && routeParam2!=''>
                    this.${routeParam2} = ${routeParam2};
                </#if>
                <#if routeParamType3!='' && routeParam3!=''>
                    this.${routeParam3} = ${routeParam3};
                </#if>
            }
        </#if>
    </#if>

    <#if screenType=='activity'>
        <#if typeRouteActivity=='3' || typeRouteActivity=='4'>
            ${className}ActivityRoute() {
                // пустой конструктор для роута с результатами
            }
        </#if>

        <#if typeRouteActivity=='2' || typeRouteActivity=='4'>
            ${className}ActivityRoute(Intent intent) {
                <#if routeParamType1!='' && routeParam1!=''>
                ${routeParam1} = <@intentRead.selectIntentReadMethod paramType=routeParamType1 extra='EXTRA_FIRST' />
                </#if>
                <#if routeParamType2!='' && routeParam2!=''>
                ${routeParam2} = <@intentRead.selectIntentReadMethod paramType=routeParamType2 extra='EXTRA_SECOND' />
                </#if>
                <#if routeParamType3!='' && routeParam3!=''>
                ${routeParam3} = <@intentRead.selectIntentReadMethod paramType=routeParamType3 extra='EXTRA_THIRD' />
                </#if>
            }
        </#if>

    @Override
    public Intent prepareIntent(Context context) {
        Intent intent = new Intent(context, ${className}${screenTypeCapitalized}View.class);
        <#if routeParamType1!='' && routeParam1!=''>
        intent.putExtra(EXTRA_FIRST, ${routeParam1});
        </#if>
        <#if routeParamType2!='' && routeParam2!=''>
        intent.putExtra(EXTRA_SECOND, ${routeParam2});
        </#if>
        <#if routeParamType3!='' && routeParam3!=''>
        intent.putExtra(EXTRA_THIRD, ${routeParam3});
        </#if>
        return intent;
    }
    <#else>
        <#if typeRouteFragment=='2'>
    ${className}FragmentRoute(Bundle args) {
                <#if routeParamType1!='' && routeParam1!=''>
                ${routeParam1} = <@bundleRead.selectBundleReadMethod paramType=routeParamType1 extra='EXTRA_FIRST' />
                </#if>
                <#if routeParamType2!='' && routeParam2!=''>
                ${routeParam2} = <@bundleRead.selectBundleReadMethod paramType=routeParamType2 extra='EXTRA_SECOND' />
                </#if>
                <#if routeParamType3!='' && routeParam3!=''>
                ${routeParam3} = <@bundleRead.selectBundleReadMethod paramType=routeParamType3 extra='EXTRA_THIRD' />
                </#if>
    }
    
    @Override
    public Bundle prepareBundle() {
        Bundle args = new Bundle();
        <#if routeParamType1!='' && routeParam1!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType1 param=routeParam1 extra='EXTRA_FIRST' />
        </#if>
        <#if routeParamType2!='' && routeParam2!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType2 param=routeParam2 extra='EXTRA_SECOND' />
        </#if>
        <#if routeParamType3!='' && routeParam3!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType3 param=routeParam3 extra='EXTRA_THIRD' />
        </#if>
        return args;
    }
        </#if>

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return ${className}${screenTypeCapitalized}View.class;
    }
    </#if>

}
