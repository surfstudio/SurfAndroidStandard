<#-- Функция, определяющая, содержит ли view LoadingState и SwipeRefreshState -->
<#function isLdsSwrView>
    <#return screenType=='activity' && typeViewActivity=='3' || screenType=='fragment' && typeViewFragment=='3'>
</#function>