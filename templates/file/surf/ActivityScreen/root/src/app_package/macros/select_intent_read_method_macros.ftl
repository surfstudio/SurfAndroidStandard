<#-- Макрос выбора intent.get в Route -->
<#macro selectIntentReadMethod paramType extra>
  <#if paramType=='String'>
        intent.getStringExtra(${extra})<#if !generateKotlin>;</#if>
    <#elseif paramType=='CharSequence'>
        intent.getCharSequenceExtra(${extra})<#if !generateKotlin>;</#if>
    <#elseif paramType=='boolean' || paramType=='Boolean'>
        intent.getBooleanExtra(${extra}, false)<#if !generateKotlin>;</#if>
    <#elseif paramType=='byte' || paramType=='Byte'>
        intent.getByteExtra(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='char' || paramType=='Char'>
        intent.getCharExtra(${extra}, ' ')<#if !generateKotlin>;</#if>
    <#elseif paramType=='short' || paramType=='Short'>
        intent.getShortExtra(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='int' || paramType=='Int'>
        intent.getIntExtra(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='long' || paramType=='Long'>
        intent.getLongExtra(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='float' || paramType=='Float'>
        intent.getFloatExtra (${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='double' || paramType=='Double'>
        intent.getDoubleExtra(${extra}, -1)<#if !generateKotlin>;</#if>
	<#else>
        intent.getSerializableExtra(${extra})<#if !generateKotlin>;</#if>
	</#if>
</#macro>
