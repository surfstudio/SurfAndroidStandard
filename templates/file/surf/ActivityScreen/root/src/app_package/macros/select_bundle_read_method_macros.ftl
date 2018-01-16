<#-- Макрос выбора args.get в Route -->
<#macro selectBundleReadMethod paramType extra>
	<#if paramType=='String'>
        args.getString(${extra})<#if !generateKotlin>;</#if>
    <#elseif paramType=='CharSequence'>
        args.getCharSequence(${extra})<#if !generateKotlin>;</#if>
    <#elseif paramType=='boolean' || paramType=='Boolean'>
        args.getBoolean(${extra}, false)<#if !generateKotlin>;</#if>
    <#elseif paramType=='byte' || paramType=='Byte'>
        args.getByte(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='char' || paramType=='Char'>
        args.getChar(${extra}, ' ')<#if !generateKotlin>;</#if>
    <#elseif paramType=='short' || paramType=='Short'>
        args.getShort(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='int' || paramType=='Int'>
        args.getInt(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='long' || paramType=='Long'>
        args.getLong(${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='float' || paramType=='Float'>
        args.getFloat (${extra}, -1)<#if !generateKotlin>;</#if>
    <#elseif paramType=='double' || paramType=='Double'>
        args.getDouble(${extra}, -1)<#if !generateKotlin>;</#if>
	<#else>
        args.getSerializable(${extra})<#if !generateKotlin>;</#if>
	</#if>
</#macro>
