<#-- Макрос выбора от какого класcа наследоваться в контроллере -->
<#macro selectTypeController>
	<#if typeController='1'>
    	BindableItemController<${nameTypeData}, ${nameController}${defPostfixController}.Holder><#elseif typeController='2'>NoDataItemController<${nameController}${defPostfixController}.Holder></#if></#macro>