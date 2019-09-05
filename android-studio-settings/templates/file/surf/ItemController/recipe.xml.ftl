<?xml version="1.0"?>
<recipe>

    <instantiate from="res/layout/item.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>
    <open file="${resOut}/layout/${layoutName}.xml"/>

	<#if language='kotlin'>
        <instantiate from="src/app_package/kotlin/ItemController.kt.ftl" to="${srcOut}/${controllerClassName}.kt"/>
        <open file="${srcOut}/${controllerClassName}.kt"/>
	<#else>
        <instantiate from="src/app_package/java/ItemController.java.ftl" to="${srcOut}/${controllerClassName}.java"/>
        <open file="${srcOut}/${controllerClassName}.java"/>
	</#if>
   
</recipe>