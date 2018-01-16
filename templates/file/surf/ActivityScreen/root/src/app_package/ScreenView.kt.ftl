<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName}


class ${className}${screenTypeCapitalized}View : <@superClass.selectTypeView /> () {

    @Inject
    lateinit var presenter: ${className}Presenter

    <#if generateToolbar>
    lateinit var toolbar: Toolbar
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    lateinit var placeHolderView: PlaceHolderViewImpl
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    </#if>

    <#if generateRecyclerView>
    lateinit var recyclerView: RecyclerView
    </#if>
    <#if (screenType=='activity' && typeViewActivity=='5') || (screenType=='fragment' && typeViewFragment=='5')>
    lateinit var adapter: PaginationableAdapter
    <#else>
    lateinit var adapter: EasyAdapter
    </#if>

    override fun getPresenters() = arrayOf(presenter)

    <#if screenType=='activity'>
        override fun createScreenConfigurator(activity: Activity, intent: Intent): ScreenConfigurator<*>
            = ${className}ScreenConfigurator(activity, intent)

        override fun createActivityConfigurator(): BaseActivityConfigurator<*, *> = ActivityConfigurator(this)

        override fun getContentView(): Int = R.layout.${layoutName}
    <#else>
        override fun createScreenConfigurator(activity: Activity, args: Bundle): ScreenConfigurator<*>
            = ${className}ScreenConfigurator(activity, args)

        override fun onCreateView(inflater: LayoutInflater,
                                  container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.${layoutName}, container, false)
        }
    </#if>

    <#if screenType=='activity'>
         override fun onCreate(savedInstanceState: Bundle,
                              persistentState: PersistableBundle?,
                              viewRecreated: Boolean) {
         findViews(window.decorView)
    <#else>
         override fun onActivityCreated(savedInstanceState: Bundle, viewRecreated: Boolean) {
                findViews(view)
    </#if>
         <#if generateToolbar>
                initToolbar()
                </#if>
                initListeners()
                <#if generateRecyclerView>
                initRecyclerView()
                </#if>
         }

    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        <#if (screenType=='activity' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='2')>
                override fun getPlaceHolderView(): PlaceHolderView = placeHolderView

            <#if (screenType=='activity' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='3')>
                override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipeRefreshLayout

                <#if (screenType='activity' && typeViewActivity!='4') || (screenType=='fragment' && typeViewFragment!='4')>
                    override fun getPaginationableAdapter(): BasePaginationableAdapter<*> {
                        return null
                    }
                </#if>
            </#if>
        </#if>

        override fun renderInternal(screenModel: ${className}ScreenModel) {}
    </#if>

    private fun findViews(view: View) {
        <#if generateToolbar>
        toolbar = view.findViewById(R.id.toolbar)
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        placeHolderView = view.findViewById(R.id.placeholder)
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        </#if>
    }

    <#if generateToolbar>
        private fun initToolbar() {}
    </#if>

        private fun initListeners() {}

    <#if generateRecyclerView>
        private fun initRecyclerView() {}
    </#if>
}