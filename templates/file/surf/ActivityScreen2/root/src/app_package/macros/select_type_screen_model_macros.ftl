<#-- Макрос выбора от какого класcа наследоваться в ScreenModel -->
<#macro selectTypeScreenModel>
	<#if typeView=='1'>
    	ScreenModel
	<#elseif typeView=='2'>
   		ScreenModel
	<#elseif typeView=='3'>
   		LdsScreenModel
	<#elseif typeView=='4'>
		LdsSwrScreenModel
	<#elseif typeView=='5'>
		LdsSwrPgnScreenModel
	</#if>
</#macro>
