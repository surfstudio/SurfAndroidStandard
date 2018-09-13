<#-- Функция, определяющая, содержит ли view LoadingState -->
<#function isLdsView>
    <#return screenType=='activity' && typeViewActivity!='1' || screenType=='fragment' && typeViewFragment!='1'>
</#function>