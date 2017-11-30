package ${packageName};

@PerScreen
class ${className}${defPostfixPresenter} extends BasePresenter<${className}${defPostfixView}> {

   private final ActivityNavigator activityNavigator;
   <#if isUseScreenModel>
   private ${className}${defPostfixScreenModel} screenModel;
   </#if>
   <#if typeRoute=='2' || typeRoute=='4'>
   ${className}${defPostfixRoute} route;
   </#if>

   @Inject
   ${className}${defPostfixPresenter}(BasePresenterDependency basePresenterDependency,
                         ActivityNavigator activityNavigator<#if typeRoute=='2' || typeRoute=='4'>, ${className}${defPostfixRoute} route</#if>) {
        super(basePresenterDependency);
        this.activityNavigator = activityNavigator;
        <#if typeRoute=='2' || typeRoute=='4'>
        this.route = route;
        </#if>
        <#if isUseScreenModel>
        screenModel = new ${className}${defPostfixScreenModel}(); //todo init screen model here
        </#if>
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);

    }
}
