<#-- Макрос выбора от какого класcа наследоваться в ScreenModel -->
<#macro selectTypeScreenModel>
	<#if generateKotlin>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
   				ScreenModel()
			<#elseif typeViewActivity=='2'>
   				LdsScreenModel()
			<#elseif typeViewActivity=='3'>
				LdsSwrScreenModel()
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
    			ScreenModel()
			<#elseif typeViewFragment=='2'>
   				LdsScreenModel()
			<#elseif typeViewFragment=='3'>
				LdsSwrScreenModel()
			</#if>
		</#if>
	<#else>
		<#if screenType=='activity'>
			<#if typeViewActivity=='1'>
    			ScreenModel
			<#elseif typeViewActivity=='2'>
   				LdsScreenModel
			<#elseif typeViewActivity=='3'>
				LdsSwrScreenModel
			</#if>
		<#else>
			<#if typeViewFragment=='1'>
    			ScreenModel
			<#elseif typeViewFragment=='2'>
   				LdsScreenModel
			<#elseif typeViewFragment=='3'>
				LdsSwrScreenModel
			</#if>
		</#if>
	</#if>
</#macro>
