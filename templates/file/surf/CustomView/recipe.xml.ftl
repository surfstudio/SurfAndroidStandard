<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
	<@kt.addAllKotlinDependencies />

    <#if generateKotlin>
        <instantiate from="src/app_package/View.kt.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}.kt" />
    <#else>
        <instantiate from="src/app_package/View.java.ftl"
            to="${escapeXmlAttribute(srcOut)}/${className}.java" />
    </#if>

    <instantiate from="res/layout/layout.xml.ftl"
        to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <open file="${escapeXmlAttribute(srcOut)}/${className}.java" />
</recipe>