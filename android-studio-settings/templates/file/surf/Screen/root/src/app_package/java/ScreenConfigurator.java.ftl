package ${packageName};

<#if screenType=='activity'>
import android.content.Intent;
<#else>
import android.os.Bundle;
</#if>

import dagger.Component;
import dagger.Module;
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.standard.ui.base.configurator.${configuratorParentClassName};
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.screen.${dependingScreenModuleClassName};

/**
 * Конфигуратор [${viewClassName}].
 */
public class ${configuratorClassName} extends ${configuratorParentClassName} {

    ${configuratorClassName}(<#if screenType=='activity'>Intent intent<#else>Bundle args</#if>) {
        super(<#if screenType=='activity'>intent<#else>args</#if>);
    }

    @Override
    protected ScreenComponent createScreenComponent(
            ActivityComponent parentComponent,
            ${dependingScreenModuleClassName} ${dependingScreenModuleVariableName},
            <#if screenType=='activity'>Intent intent<#else>Bundle args</#if>
    ) {
        return Dagger${configuratorClassName}_${screenComponentClassName}
                .builder()
                .activityComponent(parentComponent)
                .${dependingScreenModuleVariableName}(${dependingScreenModuleVariableName})
                <#if needToGenerateParams>
                .${screenName?uncap_first}ScreenModule(new ${screenModuleClassName}(${routeClassName}(<#if screenType=='activity'>intent<#else>args</#if>)))
                </#if>
                .build();
    }

    @PerScreen
    @Component(
            dependencies = ActivityComponent.class,
            modules = {${dependingScreenModuleClassName}.class<#if needToGenerateParams>, ${screenModuleClassName}.class</#if>}
    )
    public interface ${screenComponentClassName} extends ScreenComponent<${viewClassName}> {
    }
    <#if needToGenerateParams>

    @Module
    static class ${screenModuleClassName} extends CustomScreenModule<${routeClassName}> {

        ${screenModuleClassName}(${routeClassName} route) {
            super(route);
        }
    }
    </#if>
}
