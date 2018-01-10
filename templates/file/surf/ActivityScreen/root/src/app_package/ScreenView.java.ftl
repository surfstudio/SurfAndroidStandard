<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName};


public class ${className}${screenTypeCapitalized}View extends <@superClass.selectTypeView /> {

    @Inject
    ${className}Presenter presenter;

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    PlaceHolderViewImpl placeHolderView;
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    SwipeRefreshLayout swipeRefreshLayout;
    </#if>

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    <#if screenType=='activity'>
        @Override
        public ScreenConfigurator createScreenConfigurator(Activity activity, Intent intent) {
            return new ${className}ScreenConfigurator(activity, intent);
        }

        @Override
        public BaseActivityConfigurator createActivityConfigurator() {
            return new ActivityConfigurator(this);
        }
     
        @Override
        public int getContentView() {
            return R.layout.${layoutName};
        }
    <#else>
        @Override
        public ScreenConfigurator createScreenConfigurator(Activity activity, Bundle args) {
            return new ${className}ScreenConfigurator(activity, args);
        }
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        <#if (screenType=='activity' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='2')>
            @Override
            protected PlaceHolderView getPlaceHolderView() {
                return placeHolderView;
            }

            <#if (screenType=='activity' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='3')>
                @Override
                protected SwipeRefreshLayout getSwipeRefreshLayout() {
                    return swipeRefreshLayout;
                }

            <#else>
                @Override
                protected BasePaginationableAdapter getPaginationableAdapter() {
                    return null;
                }
                </#if>
            </#if>
        </#if>

        @Override
        protected void renderInternal(${className}ScreenModel screenModel) {
        }
    </#if>

}
