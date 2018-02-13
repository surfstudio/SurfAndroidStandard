<#-- Макрос выбора параметров в конструкторе(kotlin) в Route -->
<#macro create>
	<#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>
            (<#if (routeParamType1!='' && routeParam1!='') || (routeParamType2!='' && routeParam2!='') || (routeParamType3!='' && routeParam3!='')>
                <#if routeParamType1!='' && routeParam1!=''>var ${routeParam1}: ${routeParamType1}</#if><#if routeParamType2!='' && routeParam2!=''>,
                var ${routeParam2}: ${routeParamType2}</#if><#if routeParamType3!='' && routeParam3!=''>,
                var ${routeParam3}: ${routeParamType3}</#if>)
            </#if>
        </#if>
</#macro>