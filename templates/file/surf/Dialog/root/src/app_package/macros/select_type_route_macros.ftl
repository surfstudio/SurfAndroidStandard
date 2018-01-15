<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if typeRoute=='1'>
		DialogRoute
	<#else>
		DialogWithParamsRoute
	</#if>
</#macro>
