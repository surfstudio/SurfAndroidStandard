<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectIntentReadMethod paramType extra>
    <#if generateKotlin>
        <#if paramType=='String'>
            intent.getStringExtra(${extra})
        <#elseif paramType=='CharSequence'>
            intent.getCharSequenceExtra(${extra})
        <#elseif paramType=='Boolean'>
            intent.getBooleanExtra(${extra}, false)
        <#elseif paramType=='Byte'>
            intent.getByteExtra(${extra}, -1)
        <#elseif paramType=='Char'>
            intent.getCharExtra(${extra}, ' ')
        <#elseif paramType=='Short'>
            intent.getShortExtra(${extra}, -1)
        <#elseif paramType=='Int'>
            intent.getIntExtra(${extra}, -1)
        <#elseif paramType=='Long'>
            intent.getLongExtra(${extra}, -1)
        <#elseif paramType=='Float'>
            intent.getFloatExtra (${extra}, -1)
        <#elseif paramType=='Double'>
            intent.getDoubleExtra(${extra}, -1)
	    <#else>
            intent.getSerializableExtra(${extra})
	    </#if>
    <#else>
    	<#if paramType=='String'>
            intent.getStringExtra(${extra});
        <#elseif paramType=='CharSequence'>
            intent.getCharSequenceExtra(${extra});
        <#elseif paramType=='boolean'>
            intent.getBooleanExtra(${extra}, false);
        <#elseif paramType=='byte'>
            intent.getByteExtra(${extra}, -1);
        <#elseif paramType=='char'>
            intent.getCharExtra(${extra}, ' ');
        <#elseif paramType=='short'>
            intent.getShortExtra(${extra}, -1);
        <#elseif paramType=='int'>
            intent.getIntExtra(${extra}, -1);
        <#elseif paramType=='long'>
            intent.getLongExtra(${extra}, -1);
        <#elseif paramType=='float'>
            intent.getFloatExtra (${extra}, -1);
        <#elseif paramType=='double'>
            intent.getDoubleExtra(${extra}, -1);
	    <#else>
            intent.getSerializableExtra(${extra});
	    </#if>
    </#if>
</#macro>
