package ${packageName}

import android.app.Activity
<#if (screenType=='activity')>
import android.content.Intent
<#else>
import android.os.Bundle
</#if>

import dagger.Component

<#if screenType=='activity'>
class ${className}ScreenConfigurator(activity: Activity, intent: Intent) : ${screenTypeCapitalized}ScreenConfigurator(activity, intent) {
<#else>
class ${className}ScreenConfigurator(activity: Activity, args: Bundle) : ${screenTypeCapitalized}ScreenConfigurator(activity, args) {
</#if>

    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(${screenTypeCapitalized}ScreenModule::class<#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>, ${className}ScreenModule::class</#if>))
    interface ${className}ScreenComponent : ScreenComponent<${className}${screenTypeCapitalized}View> {
        //do nothing
    }

    <#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>
    @Module
    internal class ${className}ScreenModule(route: ${className}${screenTypeCapitalized}Route) : CustomScreenModule<${className}${screenTypeCapitalized}Route>(route)
    </#if>

    <#if screenType=='activity'>
    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       activityScreenModule: ActivityScreenModule?,
                                       coreActivityScreenModule: CoreActivityScreenModule?,
                                       intent: Intent?): ScreenComponent<*> {
        return Dagger${className}ScreenConfigurator_${className}ScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .coreActivityScreenModule(coreActivityScreenModule)
                <#if screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')>
                .${className?uncap_first}ScreenModule(${className}ScreenModule(${className}ActivityRoute(intent!!)))
                </#if>
                .build()
    }
    <#else>
    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                           fragmentScreenModule: FragmentScreenModule?,
                                           coreFragmentScreenModule: CoreFragmentScreenModule?,
                                           args: Bundle?): ScreenComponent<*> {
            return Dagger${className}ScreenConfigurator_${className}ScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .coreFragmentScreenModule(coreFragmentScreenModule)
                    <#if screenType=='fragment' && typeRouteFragment=='2'>
                    .${className?uncap_first}ScreenModule(${className}ScreenModule(${className}FragmentRoute(args!!)))
                    </#if>
                    .build()
        }
     </#if>
    override fun getName(): String = "${camelCaseToUnderscore(className)}"
}