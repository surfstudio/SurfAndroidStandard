<#import "macros/select_type_route_macros.ftl" as superClass>
<#import "macros/select_bundle_read_method_macros.ftl" as bundleRead>
<#import "macros/select_bundle_write_method_macros.ftl" as bundleWrite>
<#import "macros/select_args_constructor_macros.ftl" as argsConstructor>
<#import "macros/select_import_type_route_macros.ftl" as dialogRoute>

package ${packageName}

<#if typeRoute=='2'>import android.os.Bundle</#if>
<@dialogRoute.importDialogRoute />
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment

class ${className}DialogRoute<@argsConstructor.create /> : <@superClass.selectTypeRoute /> {

    <#if typeRoute=='2'>    
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
    </#if>
    override fun getFragmentClass(): Class<out CoreSimpleDialogFragment> = ${className}Dialog::class.java
}
