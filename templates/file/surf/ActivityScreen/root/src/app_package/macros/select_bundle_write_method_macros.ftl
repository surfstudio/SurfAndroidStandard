<#-- Макрос выбора args.put в Route -->
<#macro selectBundleWriteMethod paramType param extra>
	<#if paramType=='String'>
        args.putString(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='CharSequence'>
        args.putCharSequence(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='boolean' || paramType=='Boolean'>
        args.putBoolean(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='byte' || paramType=='Byte'>
        args.putByte(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='char' || paramType=='Char'>
        args.putChar(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='short' || paramType=='Short'>
        args.putShort(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='int' || paramType=='Int'>
        args.putInt(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='long' || paramType=='Long'>
        args.putLong(${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='float' || paramType=='Float'>
        args.putFloat (${extra}, ${param})<#if !generateKotlin>;</#if>
    <#elseif paramType=='double' || paramType=='Double'>
        args.putDouble(${extra}, ${param})<#if !generateKotlin>;</#if>
	<#else>
        args.putSerializable(${extra}, ${param})<#if !generateKotlin>;</#if>
	</#if>
</#macro>
