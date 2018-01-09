<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName};


public class ${className}${screenTypeCapitalized}View extends <@superClass.selectTypeView /> {

    @Inject
    ${className}Presenter presenter;

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    <#if screenType=='activity'>
        @Override
        public ScreenConfigurator createScreenConfigurator(Activity activity, Intent intent) {
            return new ${className}ScreenConfigurator(activity, intent);
        }


    <#else>
    </#if>

    

}
