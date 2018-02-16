<#import "macros/select_type_route_macros.ftl" as superClass>
<#import "macros/select_bundle_read_method_macros.ftl" as bundleRead>
<#import "macros/select_bundle_write_method_macros.ftl" as bundleWrite>
package ${packageName};

@Data
public class ${className}DialogRoute extends <@superClass.selectTypeRoute /> {

    <#if typeRoute=='2'>
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

            public ${className}DialogRoute(<#if routeParamType1!='' && routeParam1!=''>${routeParamType1} ${routeParam1}</#if><#if routeParamType2!='' && routeParam2!=''>, ${routeParamType2} ${routeParam2}</#if><#if routeParamType3!='' && routeParam3!=''>, ${routeParamType3} ${routeParam3}</#if>) {
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
    protected Class<? extends CoreSimpleDialogFragment> getFragmentClass() {
        return ${className}Dialog.class;
    }
}
