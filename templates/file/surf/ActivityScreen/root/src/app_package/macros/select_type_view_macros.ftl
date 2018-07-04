<#-- Макрос выбора от какого класcа наследоваться во Вью-->
<#macro selectTypeView>
	<#if generateKotlin>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
   				BaseRenderableActivityView<${className}ScreenModel>()
			<#elseif typeViewActivity=='2'>
   				BaseLdsActivityView<${className}ScreenModel>()
			<#elseif typeViewActivity=='3'>
				BaseLdsSwrActivityView<${className}ScreenModel>()
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
   				BaseRenderableFragmentView<${className}ScreenModel>()
			<#elseif typeViewFragment=='2'>
   				BaseLdsFragmentView<${className}ScreenModel>()
			<#elseif typeViewFragment=='3'>
				BaseLdsSwrFragmentView<${className}ScreenModel>()
			</#if>
		</#if>
	<#else>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
   				BaseRenderableActivityView<${className}ScreenModel>
			<#elseif typeViewActivity=='2'>
   				BaseLdsActivityView<${className}ScreenModel>
			<#elseif typeViewActivity=='3'>
				BaseLdsSwrActivityView<${className}ScreenModel>
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
   				BaseRenderableFragmentView<${className}ScreenModel>
			<#elseif typeViewFragment=='2'>
   				BaseLdsFragmentView<${className}ScreenModel>
			<#elseif typeViewFragment=='3'>
				BaseLdsSwrFragmentView<${className}ScreenModel>
			</#if>
		</#if>
	</#if>
</#macro>
