package ${packageName}

@PerScreen
class ${className}Presenter(basePresenterDependency: BasePresenterDependency) : BasePresenter<${className}${screenTypeCapitalized}View>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }

    <#if generateRecyclerView && hasListener>
        fun on${nameTypeData}ItemClick(${nameTypeData?uncap_first}: ${nameTypeData}) {
        }
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        fun reloadData() {
            // todo перезагрузить данные здесь
        }
    </#if>

    <#if generateRecyclerView && ((screenType=='activity' && typeViewActivity=='5') || (screenType=='fragment' && typeViewFragment=='5'))>
        fun loadMore() {
            // todo постраничная загрузка след данных
        }
    </#if>
}
