<?xml version="1.0"?>
<globals>

    <global id="srcOut" value="${escapeXmlAttribute(srcDir + '/' + slashedPackageName(packageName))}"/>
    <global id="resOut" value="${escapeXmlAttribute(resDir)}"/>
    <global id="manifestOut" value="${escapeXmlAttribute(manifestDir)}"/>

    <global id="capitalizedScreenName" value="${screenName?cap_first}"/>
    <global id="capitalizedScreenType" value="${screenType?cap_first}"/>

    <global id="viewClassName" value="${screenName?cap_first}${screenType?cap_first}View"/>
    <global id="viewParentClassName" value="BaseReact${screenType?cap_first}View"/>
    <global id="eventClassName" value="${screenName?cap_first}Event"/>
    <global id="stateHolderClassName" value="${screenName?cap_first}StateHolder"/>
    <global id="stateClassName" value="${screenName?cap_first}State"/>
    <global id="middlewareClassName" value="${screenName?cap_first}Middleware"/>
    <global id="reactorClassName" value="${screenName?cap_first}Reactor"/>
    <global id="reducerClassName" value="${screenName?cap_first}Reducer"/>

    <global id="configuratorClassName" value="${screenName?cap_first}ScreenConfigurator"/>
    <global id="configuratorParentClassName" value="${screenType?cap_first}ScreenConfigurator"/>
    <global id="screenComponentClassName" value="${screenName?cap_first}ScreenComponent"/>
    <global id="screenModuleClassName" value="${screenName?cap_first}ScreenModule"/>
    <global id="routeClassName" value="${screenName?cap_first}${screenType?cap_first}Route"/>
    <global id="routeParentClassName" value="${screenType?cap_first}CrossFeatureRoute"/>

</globals>
