<#-- Макрос для определения, какой тип контроллера нужно импортировать -->
<#macro selectImportTypeController><#if typeController='1'>BindableItemController<#else>NoDataItemController</#if></#macro>