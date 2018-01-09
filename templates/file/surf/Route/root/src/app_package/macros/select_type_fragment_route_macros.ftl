<#-- Макрос выбора от какого класcа наследоваться в Route -->
<#macro selectTypeRoute>
	<#if typeFragmentRoute=='1'>
    		FragmentRoute
	<#elseif typeFragmentRoute=='2'>
		FragmentWithParamsRoute
	</#if>
</#macro>
<#macro selectTypeRouteForImport>
	<#if typeFragmentRoute=='1'>
    		FragmentRoute;
	<#elseif typeFragmentRoute=='2'>
		FragmentWithParamsRoute;
	</#if>
</#macro>

