<?xml version="1.0"?>
<recipe>

    <#if screenType=='activity'>
        <merge from="AndroidManifest.xml.ftl" to="${manifestOut}/AndroidManifest.xml"/>
    </#if>
    
    <instantiate from="res/layout/view.xml.ftl" to="${resOut}/layout/${layoutName}.xml"/>

    <#if screenType=='activity' && mviType=='react'>
            <instantiate from="src/app_package/java/ActivityView.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
    <#elseif screenType=='activity' && mviType=='reduce'>
            <instantiate from="src/app_package/java/ReduceActivityView.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
    <#elseif screenType=='fragment' && mviType=='react'>
            <instantiate from="src/app_package/java/FragmentView.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
    <#else>
            <instantiate from="src/app_package/java/ReduceFragmentView.kt.ftl" to="${srcOut}/${viewClassName}.kt"/>
    </#if>

    <instantiate from="src/app_package/java/di/Configurator.kt.ftl" to="${srcOut}/di/${configuratorClassName}.kt"/>
    <instantiate from="src/app_package/java/Event.kt.ftl" to="${srcOut}/${eventClassName}.kt"/>

    <#if mviType='react'>
        <instantiate from="src/app_package/java/Reactor.kt.ftl" to="${srcOut}/${reactorClassName}.kt"/>
    <#else>
        <instantiate from="src/app_package/java/Reducer.kt.ftl" to="${srcOut}/${reducerClassName}.kt"/>
    </#if>

    <instantiate from="src/app_package/java/Middleware.kt.ftl" to="${srcOut}/${middlewareClassName}.kt"/>

    <#if !isMergedStateHolder>
        <instantiate from="src/app_package/java/StateHolder.kt.ftl" to="${srcOut}/${stateHolderClassName}.kt"/>
    </#if>

    <instantiate from="src/app_package/java/Route.kt.ftl" to="${srcOut}/${routeClassName}.kt"/>

</recipe>
