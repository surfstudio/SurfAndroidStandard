<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if typeDialogRoute=='1'>
    	DialogRoute
	<#elseif typeDialogRoute=='2'>
		DialogWithParamsRoute
	</#if>
</#macro>
<#macro selectTypeRouteForImport>
	<#if typeDialogRoute=='1'>
    	DialogRoute;
	<#elseif typeDialogRoute=='2'>
		DialogWithParamsRoute;
	</#if>
</#macro>
