package ${packageName};

import android.app.Activity;
<#if (screenType=='activity')>
import android.content.Intent;
<#else>
import android.os.Bundle;
</#if>

import dagger.Component;


class ${className}ScreenConfigurator extends ${screenTypeCapitalized}ScreenConfigurator {

    @PerScreen
    @Component(dependencies = AppComponent.class, modules = {${screenTypeCapitalized}ScreenModule.class<#if (screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')) || (screenType=='fragment' && typeRouteFragment=='2')>, ${className}ScreenModule.class</#if>})
    interface ${className}ScreenComponent extends ScreenComponent<${className}${screenTypeCapitalized}View> {
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
        ${className}ScreenConfigurator(Activity activity, Intent intent) {
        super(activity, intent);
    }

    @Override
    protected ScreenComponent createScreenComponent(ActivityComponent parentComponent,
                                                    ActivityScreenModule activityScreenModule,
                                                    CoreActivityScreenModule coreActivityScreenModule,
                                                    Intent intent) {
        return Dagger${className}ScreenConfigurator_${className}ScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .coreActivityScreenModule(coreActivityScreenModule)
                <#if screenType=='activity' && (typeRouteActivity=='2' || typeRouteActivity=='4')>
                .${className?uncap_first}ScreenModule(new ${className}ScreenModule(new ${className}ActivityRoute(intent)))
                </#if>
                .build();
    }
    <#else>
        ${className}ScreenConfigurator(Activity activity, Bundle args) {
        super(activity, args);
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

    @Override
    public String getName() {
        return "${camelCaseToUnderscore(className)}";
    }

}
