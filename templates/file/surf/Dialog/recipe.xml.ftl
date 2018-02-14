<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
	<@kt.addAllKotlinDependencies />

    <#if generateKotlin>
        <instantiate from="src/app_package/Route.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}DialogRoute.kt" />
        <instantiate from="src/app_package/Dialog.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}Dialog.kt" />

        <open file="${escapeXmlAttribute(srcOut)}/${className}Dialog.kt" />
    <#else>
        <instantiate from="src/app_package/Route.java.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}DialogRoute.java" />
        <instantiate from="src/app_package/Dialog.java.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}Dialog.java" />

        <open file="${escapeXmlAttribute(srcOut)}/${className}Dialog.java" />
    </#if>

    <instantiate from="res/layout/layout.xml.ftl"
        to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

</recipe>