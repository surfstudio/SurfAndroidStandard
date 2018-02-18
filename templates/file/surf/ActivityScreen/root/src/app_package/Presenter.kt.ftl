package ${packageName}

@PerScreen
class ${className}Presenter  @Inject constructor(basePresenterDependency: BasePresenterDependency) : BasePresenter<${className}${screenTypeCapitalized}View>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }

    <#if generateRecyclerView && hasListener>
        <#if typeController=='1'>
        fun on${nameTypeData}ItemClick(${nameTypeData?uncap_first}: ${nameTypeData}) {
            //todo действия при нажатии на элемент
        }
        <#elseif typeController=='2'>
        fun on${nameTypeData}ItemClick() {
            //todo действия при нажатии на элемент
        }
        </#if>
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        fun reloadData() {
            // todo перезагрузить данные здесь
        }
    </#if>

    <#if generateRecyclerView && ((screenType=='activity' && usePaginationableAdapter) || (screenType=='fragment' && usePaginationableAdapter))>
        fun loadMore() {
            // todo постраничная загрузка след данных
        }
    </#if>
}
