<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRouteForImport>
	<#if typeActivityRoute=='1'>
    		ActivityRoute;
	<#elseif typeActivityRoute=='2'>
   		ActivityWithParamsRoute;
	<#elseif typeActivityRoute=='3'>
   		ActivityWithResultRoute;
	<#elseif typeActivityRoute=='4'>
		ActivityWithParamsAndResultRoute;
	</#if>
</#macro>
<#macro selectTypeRoute>
	<#if typeActivityRoute=='1'>
    		ActivityRoute
	<#elseif typeActivityRoute=='2'>
   		ActivityWithParamsRoute
	<#elseif typeActivityRoute=='3'>
   		ActivityWithResultRoute<#if nameClassRoute!=''><${nameClassRoute}></#if>
	<#elseif typeActivityRoute=='4'>
		ActivityWithParamsAndResultRoute<#if nameClassRoute!=''><${nameClassRoute}></#if>
	</#if>
</#macro>
