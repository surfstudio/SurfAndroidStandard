package ${packageName}

<#if (screenType=='activity')>
import android.content.Intent
<#else>
import android.os.Bundle
</#if>

import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.${configuratorParentClassName}
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.${dependingScreenModuleClassName}

/**
 * Конфигуратор [${viewClassName}].
 */
class ${configuratorClassName}(<#if screenType=='activity'>intent: Intent<#else>args: Bundle?</#if>) : ${configuratorParentClassName}(<#if screenType=='activity'>intent<#else>args</#if>) {

    override fun createScreenComponent(
            parentComponent: ActivityComponent,
            ${dependingScreenModuleVariableName}: ${dependingScreenModuleClassName},
            <#if screenType=='activity'>intent: Intent<#else>args: Bundle?</#if>
    ) = Dagger${configuratorClassName}_${screenComponentClassName}
            .builder()
            .activityComponent(parentComponent)
            .${dependingScreenModuleVariableName}(${dependingScreenModuleVariableName})
            <#if needToGenerateParams>
            .${screenName?uncap_first}ScreenModule(${screenModuleClassName}(${routeClassName}(<#if screenType=='activity'>intent<#else>args</#if>)))
            </#if>
            .build()

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [${dependingScreenModuleClassName}::class<#if needToGenerateParams>, ${screenModuleClassName}::class</#if>]
    )
    interface ${screenComponentClassName} : ScreenComponent<${viewClassName}>
    <#if needToGenerateParams>

    @Module
    internal class ${screenModuleClassName}(route: ${routeClassName}) : CustomScreenModule<${routeClassName}>(route)
    </#if>
}