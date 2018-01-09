package ${packageName};

class ${className}${defPostfixScreenConfigurator} extends ActivityScreenConfigurator {

    @PerScreen
    @Component(dependencies = AppComponent.class, modules = {ActivityScreenModule.class<#if typeRoute=='2' || typeRoute=='4'>, ${className}ScreenModule.class</#if>})
    interface ${className}ScreenComponent extends ScreenComponent<${className}${defPostfixView}> {

    }

    <#if typeRoute=='2' || typeRoute=='4'>
    @Module
    static class ${className}ScreenModule extends CustomScreenModule<${className}${defPostfixRoute}>{
        public ${className}ScreenModule(${className}${defPostfixRoute} route) {
            super(route);
        }
    }
    </#if>

    public ${className}${defPostfixScreenConfigurator}(Context context, Intent intent) {
        super(context, intent);
    }

    @Override
    protected ScreenComponent createScreenComponent(AppComponent appComponent,
                                                    ActivityScreenModule activityScreenModule,
                                                    Intent intent) {
        return Dagger${className}${defPostfixScreenConfigurator}_${className}ScreenComponent.builder()
                .appComponent(appComponent)
                <#if typeRoute=='2' || typeRoute=='4'>.${className}ScreenModule(new ${className}ScreenModule(new ${className}${defPostfixRoute}(intent)))</#if>
                .activityScreenModule(activityScreenModule)
                .build();
    }

    @Override
    public String getName() {
        return "${className}";
    }

}
