<#-- Макрос выбора от какого класcа наследоваться во Вью-->
<#macro selectTypeView>
	<#if screenType=='activity'>
		<#if typeViewActivity=='1'>
    		BaseHandleableErrorActivityView
		<#elseif typeViewActivity=='2'>
   			BaseRenderableHandleableErrorActivityView<${className}ScreenModel>
		<#elseif typeViewActivity=='3'>
   			BaseLdsActivityView<${className}ScreenModel>
		<#elseif typeViewActivity=='4'>
			BaseLdsSwrActivityView<${className}ScreenModel>
		<#elseif typeViewActivity=='5'>
			BaseLdsSwrPgnActivityView<${className}ScreenModel>
		</#if>
	<#else>
		<#if typeViewFragment=='1'>
    		BaseHandleableErrorFragmentView
		<#elseif typeViewFragment=='2'>
   			BaseRenderableHandleableErrorFragmentView<${className}ScreenModel>
		<#elseif typeViewFragment=='3'>
   			BaseLdsFragmentView<${className}ScreenModel>
		<#elseif typeViewFragment=='4'>
			BaseLdsSwrFragmentView<${className}ScreenModel>
		<#elseif typeViewFragment=='5'>
			BaseLdsSwrPgnFragmentView<${className}ScreenModel>
		</#if>
	</#if>
</#macro>
