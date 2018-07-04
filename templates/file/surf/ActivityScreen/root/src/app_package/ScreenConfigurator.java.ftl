package ${packageName};

<#if (screenType=='activity')>
import android.content.Intent;
<#else>
import android.os.Bundle;
</#if>

import dagger.Component;
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator;
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.screen.ActivityScreenModule;
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator;
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule;


public class ${className}ScreenConfigurator extends ${screenTypeCapitalized}ScreenConfigurator {

    @PerScreen
    @Component(dependencies = ActivityComponent.class, modules = {${screenTypeCapitalized}ScreenModule.class<#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>, ${className}ScreenModule.class</#if>})
    public interface ${className}ScreenComponent extends ScreenComponent<${className}${screenTypeCapitalized}View> {
    }

    <#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>
    @Module
    static class ${className}ScreenModule extends CustomScreenModule<${className}${screenTypeCapitalized}Route> {
        ${className}ScreenModule(${className}${screenTypeCapitalized}Route route) {
            super(route);
        }
    }
    </#if>

    <#if screenType=='activity'>
        ${className}ScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected ScreenComponent createScreenComponent(ActivityComponent parentComponent,
                                                    ActivityScreenModule activityScreenModule,
                                                    Intent intent) {
        return Dagger${className}ScreenConfigurator_${className}ScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                <#if screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')>
                .${className?uncap_first}ScreenModule(new ${className}ScreenModule(new ${className}ActivityRoute(intent)))
                </#if>
                .build();
    }
    <#else>
        ${className}ScreenConfigurator(Bundle args) {
        super(args);
    }

    @Override
    protected ScreenComponent createScreenComponent(ActivityComponent parentComponent,
                                                    FragmentScreenModule fragmentScreenModule,
                                                    CoreFragmentScreenModule coreFragmentScreenModule,
                                                    Bundle args) {
        return Dagger${className}ScreenConfigurator_${className}ScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .coreFragmentScreenModule(coreFragmentScreenModule)
                <#if screenType=='fragment' && typeRouteFragment=='2'>
                .${className?uncap_first}ScreenModule(new ${className}ScreenModule(new ${className}FragmentRoute(args)))
                </#if>
                .build();
    }
    </#if>
}
