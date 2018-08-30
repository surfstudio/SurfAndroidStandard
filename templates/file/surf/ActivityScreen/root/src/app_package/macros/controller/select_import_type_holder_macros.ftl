<#-- Макрос для определения, какой тип ViewHolder'a нужно импортировать -->
<#macro selectImportTypeHolder><#if typeController='1'>BindableViewHolder<#else>BaseViewHolder</#if></#macro>