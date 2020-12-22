<#macro generatePutToBundleMethod paramClassName>put<@generateMethodInfix paramClassName/></#macro>

<#macro generateGetFromIntentMethod paramClassName>get<@generateMethodInfix paramClassName/>Extra</#macro>

<#macro generateGetFromBundleMethod paramClassName>get<@generateMethodInfix paramClassName/></#macro>

<#macro generateDefaultValueIfNeeded paramClassName><#if is_class_have_default_value(paramClassName)>, <@generateDefaultValue paramClassName/></#if></#macro>

<#macro generateMethodInfix paramClassName><#assign capitalizedParamClassName=paramClassName?cap_first/><#if is_primitive_type(capitalizedParamClassName)>${capitalizedParamClassName}<#else>Serializable</#if></#macro>

<#macro generateDefaultValue paramClassName><#if paramClassName=='Boolean'>false<#elseif paramClassName=='Char'>' '<#else>-1</#if></#macro>

<#function is_primitive_type className>
    <#return
        className=='String' ||
        className=='CharSequence' ||
        className=='Boolean' ||
        className=='Byte' ||
        className=='Char' ||
        className=='Short' ||
        className=='Int' ||
        className=='Integer' ||
        className=='Long' ||
        className=='Float' ||
        className=='Double'/>
</#function>

<#function is_class_have_default_value className>
    <#return
        className=='Boolean' ||
        className=='Byte' ||
        className=='Char' ||
        className=='Short' ||
        className=='Int' ||
        className=='Integer' ||
        className=='Long' ||
        className=='Float' ||
        className=='Double'/>
</#function>