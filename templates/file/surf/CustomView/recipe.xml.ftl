<?xml version="1.0"?>
<recipe>

    <#if needToGenerateLayout>
        <instantiate from="res/layout/custom_view.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>
        <open file="${resOut}/layout/${layoutName}.xml"/>
    </#if>

    <#if language='kotlin'>
        <instantiate from="src/app_package/kotlin/CustomView.kt.ftl" to="${srcOut}/${customViewClassName}.kt"/>
        <open file="${srcOut}/${customViewClassName}.kt"/>
    <#else>
        <instantiate from="src/app_package/java/CustomView.java.ftl" to="${srcOut}/${customViewClassName}.java"/>
        <open file="${srcOut}/${customViewClassName}.java"/>
    </#if>

</recipe>