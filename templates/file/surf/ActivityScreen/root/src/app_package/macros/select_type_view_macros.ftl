<#-- Макрос выбора от какого класcа наследоваться во Вью-->
<#macro selectTypeView>
	<#if generateKotlin>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
    			BaseHandleableErrorActivityView()
			<#elseif typeViewActivity=='2'>
   				BaseRenderableHandleableErrorActivityView<${className}ScreenModel>()
			<#elseif typeViewActivity=='3'>
   				BaseLdsActivityView<${className}ScreenModel>()
			<#elseif typeViewActivity=='4'>
				BaseLdsSwrActivityView<${className}ScreenModel>()
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
    			BaseHandleableErrorFragmentView()
			<#elseif typeViewFragment=='2'>
   				BaseRenderableHandleableErrorFragmentView<${className}ScreenModel>()
			<#elseif typeViewFragment=='3'>
   				BaseLdsFragmentView<${className}ScreenModel>()
			<#elseif typeViewFragment=='4'>
				BaseLdsSwrFragmentView<${className}ScreenModel>()
			</#if>
		</#if>
	<#else>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
    			BaseHandleableErrorActivityView
			<#elseif typeViewActivity=='2'>
   				BaseRenderableHandleableErrorActivityView<${className}ScreenModel>
			<#elseif typeViewActivity=='3'>
   				BaseLdsActivityView<${className}ScreenModel>
			<#elseif typeViewActivity=='4'>
				BaseLdsSwrActivityView<${className}ScreenModel>
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
			</#if>
		</#if>
	</#if>
</#macro>
