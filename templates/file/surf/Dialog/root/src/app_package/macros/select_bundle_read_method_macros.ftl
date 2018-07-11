<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectBundleReadMethod paramType extra>
    <#if generateKotlin>
        <#if paramType=='String'>
            args.getString(${extra})
        <#elseif paramType=='CharSequence'>
            args.getCharSequence(${extra})
        <#elseif paramType=='Boolean'>
            args.getBoolean(${extra}, false)
        <#elseif paramType=='Byte'>
            args.getByte(${extra}, -1)
        <#elseif paramType=='Char'>
            args.getChar(${extra}, ' ')
        <#elseif paramType=='Short'>
            args.getShort(${extra}, -1)
        <#elseif paramType=='Int'>
            args.getInt(${extra}, -1)
        <#elseif paramType=='Long'>
            args.getLong(${extra}, -1)
        <#elseif paramType=='Float'>
            args.getFloat (${extra}, -1)
        <#elseif paramType=='Double'>
            args.getDouble(${extra}, -1)
        <#else>
            args.getSerializable(${extra})
        </#if>
    <#else>
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
    </#if>
</#macro>
