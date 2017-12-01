<?xml version="1.0"?>
<globals>
	<#include "root://activities/common/common_globals.xml.ftl" />
    <global id="manifestOut" value="${manifestDir}" />
    <global id="srcOut" value="${srcDir}/${slashedPackageName(packageName)}" />
    <global id="resOut" value="${resDir}" />
    <global id="nameParam" value="${extractLetters(nameTypeData?lower_case)}" />
    
    <global id="defPostfixController" value="ItemController" />
    <global id="ktOrJavaExt" type="string" value="${generateKotlin?string('kt','java')}" />
</globals>
