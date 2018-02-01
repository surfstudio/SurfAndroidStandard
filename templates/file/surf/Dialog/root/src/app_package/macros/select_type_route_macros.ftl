<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if generateKotlin>
		<#if typeRoute=='1'>
			DialogRoute()
		<#else>
			DialogWithParamsRoute()
		</#if>
	<#else>
		<#if typeRoute=='1'>
			DialogRoute
		<#else>
			DialogWithParamsRoute
		</#if>
	</#if>
</#macro>
