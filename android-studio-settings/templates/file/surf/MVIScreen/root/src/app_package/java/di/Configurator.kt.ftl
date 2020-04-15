<#if (screenType=='activity')>
    <#assign argsImport = "import android.content.Intent">
    <#assign argsType ="Intent">
    <#assign args = "intent">
    <#assign parentScreenModule ="ActivityScreenModule">
<#else>
    <#assign argsImport = "import android.os.Bundle">
    <#assign argsType ="Bundle">
    <#assign args = "bundle">
    <#assign parentScreenModule ="FragmentScreenModule">
</#if>

<#if (mviType=='react')>
    <#assign binderReactorVar = "reactor">
    <#assign binderReactorVarType = reactorClassName>
<#else>
    <#assign binderReactorVar = "reducer">
    <#assign binderReactorVarType = reducerClassName>
</#if>

package ${packageName}.di

${argsImport}
import dagger.Component
import dagger.Module
import dagger.Provides
import ${packageName}.*
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
<#if applicationPackage??>
import ${applicationPackage}.ui.activity.di.ActivityComponent
import ${applicationPackage}.ui.activity.di.${configuratorParentClassName}
import ${applicationPackage}.ui.screen.CustomScreenModule
import ${applicationPackage}.ui.screen.${parentScreenModule}
</#if>

/**
 * Конфигуратор [${viewClassName}].
 */
class ${configuratorClassName}(${args}: ${argsType}) : ${configuratorParentClassName}(${args}) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [${parentScreenModule}::class, ${screenModuleClassName}::class]
    )
    interface ${screenComponentClassName} : BindableScreenComponent<${viewClassName}>

    @Module
    internal class ${screenModuleClassName}(route: ${routeClassName}) :
        CustomScreenModule<${routeClassName}>(route){

        @Provides
        @PerScreen
        fun provideEventHub(screenEventHubDependency: ScreenEventHubDependency) =
            ScreenEventHub<${eventClassName}>(screenEventHubDependency, ${eventClassName}::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<${eventClassName}>,
            middleware: ${middlewareClassName},
            ${binderReactorVar}: ${binderReactorVarType},
            stateHolder: ${stateHolderClassName}
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, middleware, stateHolder, ${binderReactorVar})
        }
    }

    override fun createScreenComponent(
            parentComponent: ActivityComponent,
            ${parentScreenModule?uncap_first}: ${parentScreenModule},
            ${args}: ${argsType}
    ): ${screenComponentClassName} {
        return Dagger${configuratorClassName}_${screenComponentClassName}.builder()
            .activityComponent(parentComponent)
            .${parentScreenModule?uncap_first}(${parentScreenModule?uncap_first})
            .${screenModuleClassName?uncap_first}(${screenModuleClassName}(${routeClassName}()))
            .build()
    }
}
