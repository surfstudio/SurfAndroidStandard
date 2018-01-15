<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectBundleWriteMethod paramType param extra>
	<#if paramType=='String'>
        args.putString(${extra}, ${param});
    <#elseif paramType=='CharSequence'>
        args.putCharSequence(${extra}, ${param});
    <#elseif paramType=='boolean'>
        args.putBoolean(${extra}, ${param});
    <#elseif paramType=='byte'>
        args.putByte(${extra}, ${param});
    <#elseif paramType=='char'>
        args.putChar(${extra}, ${param});
    <#elseif paramType=='short'>
        args.putShort(${extra}, ${param});
    <#elseif paramType=='int'>
        args.putInt(${extra}, ${param});
    <#elseif paramType=='long'>
        args.putLong(${extra}, ${param});
    <#elseif paramType=='float'>
        args.putFloat (${extra}, ${param});
    <#elseif paramType=='double'>
        args.putDouble(${extra}, ${param});
	<#else>
        args.putSerializable(${extra}, ${param});
	</#if>
</#macro>
