<?xml version="1.0"?>
<recipe>

    <#if screenType=='activity'>
        <merge from="AndroidManifest.xml.ftl" to="${manifestOut}/AndroidManifest.xml"/>
    </#if>
    
    <instantiate from="res/layout/view.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>

    <instantiate from="src/app_package/kotlin/View.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
    <instantiate from="src/app_package/kotlin/Presenter.kt.ftl" to="${srcOut}/${presenterClassName}.kt"/>
    <instantiate from="src/app_package/kotlin/BindModel.kt.ftl" to="${srcOut}/${bindModelClassName}.kt"/>
    <instantiate from="src/app_package/kotlin/Route.kt.ftl" to="${srcOut}/${routeClassName}.kt"/>
    <instantiate from="src/app_package/kotlin/ScreenConfigurator.kt.ftl" to="${srcOut}/${configuratorClassName}.kt"/>

</recipe>
