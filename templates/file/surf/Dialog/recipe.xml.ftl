<?xml version="1.0"?>
<recipe>

    <instantiate from="res/layout/dialog.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>
    <open file="${resOut}/layout/${layoutName}.xml"/>

    <#if language='kotlin'>
        <instantiate from="src/app_package/kotlin/Route.kt.ftl" to="${srcOut}/${dialogRouteClassName}.kt"/>
        <instantiate from="src/app_package/kotlin/Dialog.kt.ftl" to="${srcOut}/${dialogClassName}.kt"/>
        <open file="${srcOut}/${dialogClassName}.kt"/>
    <#else>
        <instantiate from="src/app_package/java/Route.java.ftl" to="${srcOut}/${dialogRouteClassName}.java"/>
        <instantiate from="src/app_package/java/Dialog.java.ftl" to="${srcOut}/${dialogClassName}.java"/>
        <open file="${srcOut}/${dialogClassName}.java"/>
    </#if>

</recipe>