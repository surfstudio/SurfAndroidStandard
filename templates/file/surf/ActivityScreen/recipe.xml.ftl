<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
    <@kt.addAllKotlinDependencies />

    <#if screenType=='activity'>
        <merge from="AndroidManifest.xml.ftl" 
            to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />
    </#if>

    <#if generateKotlin>
        <instantiate from="src/app_package/ScreenConfigurator.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}ScreenConfigurator.kt" />
        <instantiate from="src/app_package/ScreenModel.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}ScreenModel.kt" />
        <instantiate from="src/app_package/Presenter.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}Presenter.kt" />
        <instantiate from="src/app_package/ScreenView.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}View.kt" />
        <instantiate from="src/app_package/Route.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}Route.kt" />

        <open file="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}View.kt" />
    <#else>
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

        <open file="${escapeXmlAttribute(srcOut)}/${className}${screenTypeCapitalized}View.java" />
    </#if>

     <instantiate from="res/layout/layout.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

</recipe>