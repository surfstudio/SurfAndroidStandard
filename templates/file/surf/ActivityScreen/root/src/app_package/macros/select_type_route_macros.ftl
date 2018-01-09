<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if screenType=='activity'>
		<#if typeRouteActivity=='2'>
			ActivityWithParamsRoute
		<#elseif typeRouteActivity=='3'>
			ActivityWithResultRoute<${routeResult}>
		<#elseif typeRouteActivity=='4'>
			ActivityWithParamsAndResultRoute<${routeResult}>
		<#else>
			ActivityRoute
		</#if>
	<#else>
		<#if typeRouteFragment=='2'>
			FragmentWithParamsRoute
		<#else>
			FragmentRoute
		</#if>
	</#if>
</#macro>
