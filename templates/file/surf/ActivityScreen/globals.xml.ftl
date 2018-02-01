<?xml version="1.0"?>
<globals>
 	<#include "root://activities/common/common_globals.xml.ftl" />

    <global id="resOut" value="${resDir}" />
    <global id="srcOut" value="${srcDir}/${slashedPackageName(packageName)}" />
    <global id="relativePackage" value="<#if relativePackage?has_content>${relativePackage}<#else>${packageName}</#if>" />

    <global id="screenTypeCapitalized" value="${screenType?capitalize}" />

    <#-- копи-паст из шаблонов ItemController -->
    <global id="nameParam" value="${extractLetters(nameTypeData?lower_case)}" />
    <global id="defPostfixController" value="ItemController" />
</globals>