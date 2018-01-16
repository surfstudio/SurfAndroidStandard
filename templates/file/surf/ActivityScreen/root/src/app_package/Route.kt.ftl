<#import "macros/select_type_route_macros.ftl" as superClass>
<#import "macros/select_intent_read_method_macros.ftl" as intentRead>
<#import "macros/select_bundle_read_method_macros.ftl" as bundleRead>
<#import "macros/select_bundle_write_method_macros.ftl" as bundleWrite>
<#import "macros/select_args_constructor_macros.ftl" as argsConstructor>
package ${packageName}

class ${className}${screenTypeCapitalized}Route<@argsConstructor.create /> : <@superClass.selectTypeRoute />() {

    <#if screenType=='activity'>

        <#if typeRouteActivity=='2' || typeRouteActivity=='4'>
            constructor(intent: Intent) : this(
            <#if routeParamType1!='' && routeParam1!=''>
                ${routeParam1} = <@intentRead.selectIntentReadMethod paramType=routeParamType1 extra='EXTRA_FIRST' />
            </#if>
            <#if routeParamType2!='' && routeParam2!=''>
                , ${routeParam2} = <@intentRead.selectIntentReadMethod paramType=routeParamType2 extra='EXTRA_SECOND' />
            </#if>
            <#if routeParamType3!='' && routeParam3!=''>
                , ${routeParam3} = <@intentRead.selectIntentReadMethod paramType=routeParamType3 extra='EXTRA_THIRD' />
            </#if>)
        </#if>

     override fun prepareIntent(context: Context): Intent {
        val intent = Intent(context, ${className}${screenTypeCapitalized}View::class.java)
        <#if routeParamType1!='' && routeParam1!=''>
        intent.putExtra(EXTRA_FIRST, ${routeParam1})
        </#if>
        <#if routeParamType2!='' && routeParam2!=''>
        intent.putExtra(EXTRA_SECOND, ${routeParam2})
        </#if>
        <#if routeParamType3!='' && routeParam3!=''>
        intent.putExtra(EXTRA_THIRD, ${routeParam3})
        </#if>
        return intent
    }
    <#else>
        <#if typeRouteFragment=='2'>
    constructor(args: Bundle) : this (
                <#if routeParamType1!='' && routeParam1!=''>
                ${routeParam1} = <@bundleRead.selectBundleReadMethod paramType=routeParamType1 extra='EXTRA_FIRST' />
                </#if>
                <#if routeParamType2!='' && routeParam2!=''>
                , ${routeParam2} = <@bundleRead.selectBundleReadMethod paramType=routeParamType2 extra='EXTRA_SECOND' />
                </#if>
                <#if routeParamType3!='' && routeParam3!=''>
                , ${routeParam3} = <@bundleRead.selectBundleReadMethod paramType=routeParamType3 extra='EXTRA_THIRD' />
                </#if>)
        </#if>

    override fun prepareBundle(): Bundle {
        val args = Bundle()
        <#if routeParamType1!='' && routeParam1!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType1 param=routeParam1 extra='EXTRA_FIRST' />
        </#if>
        <#if routeParamType2!='' && routeParam2!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType2 param=routeParam2 extra='EXTRA_SECOND' />
        </#if>
        <#if routeParamType3!='' && routeParam3!=''>
        <@bundleWrite.selectBundleWriteMethod paramType=routeParamType3 param=routeParam3 extra='EXTRA_THIRD' />
        </#if>
        return args
    }

    override fun getFragmentClass(): Class<out Fragment>  = ${className}${screenTypeCapitalized}View::class.java
    </#if>
}