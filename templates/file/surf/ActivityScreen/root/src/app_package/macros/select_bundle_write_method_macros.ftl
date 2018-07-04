<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectBundleWriteMethod paramType param extra>
    <#if generateKotlin>
        <#if paramType=='String'>
            args.putString(${extra}, ${param})
        <#elseif paramType=='CharSequence'>
            args.putCharSequence(${extra}, ${param})
        <#elseif paramType=='Boolean'>
            args.putBoolean(${extra}, ${param})
        <#elseif paramType=='Byte'>
            args.putByte(${extra}, ${param})
        <#elseif paramType=='Char'>
            args.putChar(${extra}, ${param})
        <#elseif paramType=='Short'>
            args.putShort(${extra}, ${param})
        <#elseif paramType=='Int'>
            args.putInt(${extra}, ${param})
        <#elseif paramType=='Long'>
            args.putLong(${extra}, ${param})
        <#elseif paramType=='Float'>
            args.putFloat (${extra}, ${param})
        <#elseif paramType=='Double'>
            args.putDouble(${extra}, ${param})
	    <#else>
            args.putSerializable(${extra}, ${param})
	    </#if>
    <#else>
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
    </#if>
</#macro>
