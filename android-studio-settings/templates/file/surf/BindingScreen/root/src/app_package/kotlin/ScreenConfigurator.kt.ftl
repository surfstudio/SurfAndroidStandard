package ${packageName}

<#if (screenType=='activity')>
import android.content.Intent
<#else>
import android.os.Bundle
</#if>

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen


/**
 * Конфигуратор [${viewClassName}].
 */
class ${configuratorClassName}(<#if screenType=='activity'>intent: Intent<#else>args: Bundle</#if>) : ${configuratorParentClassName}(<#if screenType=='activity'>intent<#else>args</#if>) {

    override fun createScreenComponent(
            parentComponent: ActivityComponent,
            ${dependingScreenModuleVariableName}: ${dependingScreenModuleClassName},
            <#if screenType=='activity'>intent: Intent<#else>args: Bundle</#if>
    ): ${screenComponentClassName} = Dagger${configuratorClassName}_${screenComponentClassName}
            .builder()
            .activityComponent(parentComponent)
            .${dependingScreenModuleVariableName}(${dependingScreenModuleVariableName})
            .${screenName?uncap_first}ScreenModule(${screenModuleClassName}(<#if needToGenerateParams>${routeClassName}(<#if screenType=='activity'>intent<#else>args</#if>)</#if>))
            .build()

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [${dependingScreenModuleClassName}::class, ${screenModuleClassName}::class]
    )
    interface ${screenComponentClassName} : BindableScreenComponent<${viewClassName}>

    @Module
    internal class ${screenModuleClassName} <#if needToGenerateParams>(route: ${routeClassName}): CustomScreenModule<${routeClassName}>(route)</#if>{

        @Provides
        @PerScreen
        fun providePresenter(presenter: ${presenterClassName}) = Any()
    }

}