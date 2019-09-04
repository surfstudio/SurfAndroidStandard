<?xml version="1.0"?>
<globals>

    <global id="srcOut" value="${escapeXmlAttribute(srcDir + '/' + slashedPackageName(packageName))}"/>
    <global id="resOut" value="${escapeXmlAttribute(resDir)}"/>
    <global id="manifestOut" value="${escapeXmlAttribute(manifestDir)}"/>

    <global id="capitalizedScreenName" value="${screenName?cap_first}"/>
    <global id="capitalizedScreenType" value="${screenType?cap_first}"/>

    <global id="viewClassName" value="${screenName?cap_first}${screenType?cap_first}View"/>
    <global id="presenterClassName" value="${screenName?cap_first}${screenType?cap_first}View"/>
    <global id="screenModelClassName" value="${screenName?cap_first}ScreenModel"/>
    <global id="routeClassName" value="${screenName?cap_first}Route"/>
    <global id="configuratorClassName" value="${screenName?cap_first}ScreenConfigurator"/>
    <global id="screenModuleClassName" value="${screenName?cap_first}ScreenModule"/>
    <global id="screenComponentClassName" value="${screenName?cap_first}ScreenComponent"/>
    <global id="viewParentClassName" value="Base<#if !needToGenerateLds && !needToGenerateSwr && !needToGeneratePgn>Renderable<#else><#if needToGenerateLds>Lds</#if><#if needToGenerateSwr>Swr</#if><#if needToGeneratePgn>Pgn</#if></#if>${screenType?cap_first}View"/>
    <global id="routeParentClassName" value="${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route"/>
    <global id="configuratorParentClassName" value="${screenType?cap_first}ScreenConfigurator"/>
    <global id="screenModelParentClassName" value="<#if needToGenerateLds>Lds</#if><#if needToGenerateSwr>Swr</#if><#if needToGeneratePgn>Pgn</#if>ScreenModel"/>
    <global id="dependingScreenModuleClassName" value="${screenType?cap_first}ScreenModule"/>
    <global id="dependingScreenModuleVariableName" value="${screenType}ScreenModule"/>

</globals>