<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
	<@kt.addAllKotlinDependencies />
    <instantiate from="src/app_package/Controller.${ktOrJavaExt}.ftl"
             to="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.${ktOrJavaExt}" />
    <open file="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.${ktOrJavaExt}" />
    <#if generateLayout>
    <instantiate from="res/layout/layout.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${nameRes}.xml" />
    </#if>
</recipe>
