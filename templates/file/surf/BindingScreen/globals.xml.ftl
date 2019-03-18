<?xml version="1.0"?>
<globals>

    <global id="srcOut" value="${escapeXmlAttribute(srcDir + '/' + slashedPackageName(packageName))}"/>
    <global id="resOut" value="${escapeXmlAttribute(resDir)}"/>
    <global id="manifestOut" value="${escapeXmlAttribute(manifestDir)}"/>

    <global id="capitalizedScreenName" value="${screenName?cap_first}"/>
    <global id="capitalizedScreenType" value="${screenType?cap_first}"/>

    <global id="viewClassName" value="${screenName?cap_first}${screenType?cap_first}View"/>
    <global id="presenterClassName" value="${screenName?cap_first}${screenType?cap_first}Presenter"/>
    <global id="bindModelClassName" value="${screenName?cap_first}BindModel"/>
    <global id="routeClassName" value="${screenName?cap_first}Route"/>
    <global id="configuratorClassName" value="${screenName?cap_first}ScreenConfigurator"/>
    <global id="screenModuleClassName" value="${screenName?cap_first}ScreenModule"/>
    <global id="screenComponentClassName" value="${screenName?cap_first}ScreenComponent"/>
    <global id="viewParentClassName" value="BaseRx${screenType?cap_first}View"/>
    <global id="routeParentClassName" value="${screenType?cap_first}<#if crossFeature>CrossFeature</#if><#if needToGenerateParams && needToGenerateResult>WithParamsAndResult<#elseif needToGenerateParams>WithParams<#elseif needToGenerateParams>WithResult</#if>Route"/>
    <global id="configuratorParentClassName" value="${screenType?cap_first}ScreenConfigurator"/>
    <global id="dependingScreenModuleClassName" value="${screenType?cap_first}ScreenModule"/>
    <global id="dependingScreenModuleVariableName" value="${screenType}ScreenModule"/>

</globals>