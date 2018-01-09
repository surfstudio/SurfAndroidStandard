<#-- Макрос выбора от какого класcа наследоваться во Вью-->
<#macro selectTypeView>
	<#if screenType=='activity'>
		<#if typeViewActivity=='1'>
    		BaseHandleableErrorActivityView
		<#elseif typeViewActivity=='2'>
   			BaseRenderableHandleableErrorActivityView
		<#elseif typeViewActivity=='3'>
   			BaseLdsActivityView
		<#elseif typeViewActivity=='4'>
			BaseLdsSwrActivityView
		<#elseif typeViewActivity=='5'>
			BaseLdsSwrPgnActivityView
		</#if>
	<#else>
		<#if typeViewFragment=='1'>
    		BaseHandleableErrorFragmentView
		<#elseif typeViewFragment=='2'>
   			BaseRenderableHandleableErrorFragmentView
		<#elseif typeViewFragment=='3'>
   			BaseLdsFragmentView
		<#elseif typeViewFragment=='4'>
			BaseLdsSwrFragmentView
		<#elseif typeViewFragment=='5'>
			BaseLdsSwrPgnFragmentView
		</#if>
	</#if>
</#macro>
