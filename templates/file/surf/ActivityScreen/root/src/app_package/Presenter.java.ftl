package ${packageName};

@PerScreen
class ${className}Presenter extends BasePresenter<${className}${screenTypeCapitalized}View> {

    public ${className}Presenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);
    }

    <#if generateRecyclerView && hasListener>
        void on${nameTypeData}ItemClick(${nameTypeData} ${nameTypeData?uncap_first}) {
        }
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        void reloadData() {
            // todo перезагрузить данные здесь
        }
    </#if>

    <#if generateRecyclerView && ((screenType=='activity' && typeViewActivity=='5') || (screenType=='fragment' && typeViewFragment=='5'))>
        void loadMore() {
            // todo постраничная загрузка след данных
        }
    </#if>
}
