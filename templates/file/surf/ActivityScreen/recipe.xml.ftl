<?xml version="1.0"?>
<recipe>

    <#if screenType=='activity'>
        <merge from="AndroidManifest.xml.ftl" 
            to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />
    </#if>

    <instantiate from="src/app_package/Presenter.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Presenter.java" />
    <instantiate from="src/app_package/Route.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}Route.java" />
    <instantiate from="src/app_package/ScreenConfigurator.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}ScreenConfigurator.java" />
    <instantiate from="src/app_package/ScreenView.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}View.java" />
    
    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        <instantiate from="src/app_package/ScreenModel.java.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}ScreenModel.java" />
    </#if>

</recipe>