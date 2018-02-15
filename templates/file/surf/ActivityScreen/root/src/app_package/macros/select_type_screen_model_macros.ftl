<#-- Макрос выбора от какого класcа наследоваться в ScreenModel -->
<#macro selectTypeScreenModel>
	<#if generateKotlin>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
    			ScreenModel()
			<#elseif typeViewActivity=='2'>
   				ScreenModel()
			<#elseif typeViewActivity=='3'>
   				LdsScreenModel()
			<#elseif typeViewActivity=='4'>
				LdsSwrScreenModel()
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
    			ScreenModel()
			<#elseif typeViewFragment=='2'>
   				ScreenModel()
			<#elseif typeViewFragment=='3'>
   				LdsScreenModel()
			<#elseif typeViewFragment=='4'>
				LdsSwrScreenModel()
			</#if>
		</#if>
	<#else>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
    			ScreenModel
			<#elseif typeViewActivity=='2'>
   				ScreenModel
			<#elseif typeViewActivity=='3'>
   				LdsScreenModel
			<#elseif typeViewActivity=='4'>
				LdsSwrScreenModel
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
    			ScreenModel
			<#elseif typeViewFragment=='2'>
   				ScreenModel
			<#elseif typeViewFragment=='3'>
   				LdsScreenModel
			<#elseif typeViewFragment=='4'>
				LdsSwrScreenModel
			</#if>
		</#if>
	</#if>
</#macro>
