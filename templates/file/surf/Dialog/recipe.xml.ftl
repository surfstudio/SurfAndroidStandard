<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
	<@kt.addAllKotlinDependencies />

    <instantiate from="src/app_package/Route.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}DialogRoute.java" />
    <instantiate from="src/app_package/Dialog.java.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Dialog.java" />

    <instantiate from="res/layout/layout.xml.ftl"
        to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <open file="${escapeXmlAttribute(srcOut)}/${className}Dialog.java" />
</recipe>