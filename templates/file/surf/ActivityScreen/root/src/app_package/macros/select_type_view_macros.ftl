<#-- Макрос выбора от какого класcа наследоваться во Вью-->
<#macro selectTypeView>
	<#if typeView=='1'>
    	BaseHandleableErrorActivityView<#if isUseScreenModel><${className}${defPostfixScreenModel}></#if>
	<#elseif typeView=='2'>
   		BaseRenderableHandleableErrorActivityView<#if isUseScreenModel><${className}${defPostfixScreenModel}></#if>
	<#elseif typeView=='3'>
   		BaseLdsActivityView<#if isUseScreenModel><${className}${defPostfixScreenModel}></#if>
	<#elseif typeView=='4'>
		BaseLdsSwrActivityView<#if isUseScreenModel><${className}${defPostfixScreenModel}></#if>
	<#elseif typeView=='5'>
		BaseLdsSwrPgnActivityView<#if isUseScreenModel><${className}${defPostfixScreenModel}></#if>
	</#if>
</#macro>
