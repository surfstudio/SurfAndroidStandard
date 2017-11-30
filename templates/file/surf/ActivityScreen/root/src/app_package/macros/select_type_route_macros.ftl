<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if typeRoute=='1'>
    		ActivityRoute
	<#elseif typeRoute=='2'>
   		ActivityWithParamsRoute
	<#elseif typeRoute=='3'>
   		ActivityWithResultRoute<#if nameClassRoute!=''><${nameClassRoute}></#if>
	<#elseif typeRoute=='4'>
		ActivityWithParamsAndResultRoute<#if nameClassRoute!=''><${nameClassRoute}></#if>
	</#if>
</#macro>
