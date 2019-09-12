<?xml version="1.0"?>
<recipe>

    <#if screenType=='activity'>
        <merge from="AndroidManifest.xml.ftl" to="${manifestOut}/AndroidManifest.xml"/>
    </#if>
    
    <instantiate from="res/layout/view.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>

    <#if language='kotlin'>
        <instantiate from="src/app_package/kotlin/View.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
        <instantiate from="src/app_package/kotlin/Presenter.kt.ftl" to="${srcOut}/${presenterClassName}.kt"/>
        <instantiate from="src/app_package/kotlin/ScreenModel.kt.ftl" to="${srcOut}/${screenModelClassName}.kt"/>
        <instantiate from="src/app_package/kotlin/Route.kt.ftl" to="${srcOut}/${routeClassName}.kt"/>
        <instantiate from="src/app_package/kotlin/ScreenConfigurator.kt.ftl" to="${srcOut}/${configuratorClassName}.kt"/>
    <#else>
        <instantiate from="src/app_package/java/View.java.ftl" to="${srcOut}/${viewClassName}.java"/>
        <instantiate from="src/app_package/java/Presenter.java.ftl" to="${srcOut}/${presenterClassName}.java"/>
        <instantiate from="src/app_package/java/ScreenModel.java.ftl" to="${srcOut}/${screenModelClassName}.java"/>
        <instantiate from="src/app_package/java/Route.java.ftl" to="${srcOut}/${routeClassName}.java"/>
        <instantiate from="src/app_package/java/ScreenConfigurator.java.ftl" to="${srcOut}/${configuratorClassName}.java"/>
    </#if>

</recipe>
