<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectBundleReadMethod paramType extra>
	<#if paramType=='String'>
        args.getString(${extra});
    <#elseif paramType=='CharSequence'>
        args.getCharSequence(${extra});
    <#elseif paramType=='boolean'>
        args.getBoolean(${extra}, false);
    <#elseif paramType=='byte'>
        args.getByte(${extra}, -1);
    <#elseif paramType=='char'>
        args.getChar(${extra}, ' ');
    <#elseif paramType=='short'>
        args.getShort(${extra}, -1);
    <#elseif paramType=='int'>
        args.getInt(${extra}, -1);
    <#elseif paramType=='long'>
        args.getLong(${extra}, -1);
    <#elseif paramType=='float'>
        args.getFloat (${extra}, -1);
    <#elseif paramType=='double'>
        args.getDouble(${extra}, -1);
	<#else>
        args.getSerializable(${extra});
	</#if>
</#macro>
