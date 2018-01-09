<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
	<@kt.addAllKotlinDependencies />

	<#if generateKotlin>
     <instantiate from="src/app_package/Controller.kt.ftl"
             to="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.kt" />
    <open file="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.kt" />
	<#else>
     <instantiate from="src/app_package/Controller.java.ftl"
             to="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.java" />
    <open file="${escapeXmlAttribute(srcOut)}/${nameController}${defPostfixController}.java" />
	</#if>
   
    <#if generateLayout>
    <instantiate from="res/layout/layout.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${nameRes}.xml" />
    </#if>
</recipe>
