<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName}

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.activity.BaseLdsSwrActivityView
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.fragment.BaseLdsFragmentView
import ru.surfstudio.android.core.mvp.fragment.BaseLdsSwrFragmentView
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.standard.base_ui.recylcer.adapter.PaginationableAdapter
<#if applicationPackage??>import ${applicationPackage}.R</#if>
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.placeholder.PlaceHolderView
import javax.inject.Inject

<#if generateToolbar>
import android.support.v7.widget.Toolbar
</#if>
import android.view.LayoutInflater
import android.view.ViewGroup

class ${className}${screenTypeCapitalized}View : <@superClass.selectTypeView /> {

    @Inject
    lateinit var presenter: ${className}Presenter
    <#if generateToolbar>

    private lateinit var toolbar: Toolbar
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>

    private lateinit var placeHolderView: PlaceHolderViewImpl
    </#if>
    <#if (screenType=='activity' && typeViewActivity=='3') || (screenType=='fragment' && typeViewFragment=='3')>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    </#if>
    <#if generateRecyclerView>

    private lateinit var recyclerView: RecyclerView
        <#if usePaginationableAdapter>

        private val adapter = PaginationableAdapter<${nameTypeData}>()
        <#else>

        private val adapter = EasyAdapter()
        </#if>

    private val ${nameController?uncap_first}${defPostfixController} = ${nameController}${defPostfixController}(<#if hasListener>presenter::on${nameTypeData}ItemClick</#if>)
    </#if>

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)
    <#if screenType=='activity'>

        override fun createConfigurator(): ActivityScreenConfigurator = ${className}ScreenConfigurator(intent)

        @LayoutRes
        override fun getContentView(): Int = R.layout.${layoutName}
    <#else>

        override fun createConfigurator(): FragmentScreenConfigurator = ${className}ScreenConfigurator(arguments)

        override fun onCreateView(inflater: LayoutInflater,
                                  container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.${layoutName}, container, false)
        }
    </#if>
    <#if screenType=='activity'>

    override fun onCreate(savedInstanceState: Bundle?,
                         persistentState: PersistableBundle?,
                         viewRecreated: Boolean) {
        findViews(window.decorView)
    <#else>

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
         findViews(view!!)
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

    override fun getPlaceHolderView(): PlaceHolderView = placeHolderView
    </#if>
    <#if (screenType=='activity' && typeViewActivity=='3') || (screenType=='fragment' && typeViewFragment=='3')>

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipeRefreshLayout
    </#if>

    override fun renderInternal(screenModel: ${className}ScreenModel) {
    <#if generateRecyclerView>
        <#if usePaginationableAdapter>
                        adapter.setItems(ItemList.create()
                            .addAll(screenModel.itemList, ${nameController?uncap_first}${defPostfixController}), screenModel.paginationState)
        <#else>
                        adapter.setItems(ItemList.create()
                            .addAll(screenModel.itemList, ${nameController?uncap_first}${defPostfixController}))
        </#if>
    </#if>
    }

    private fun findViews(view: View) {
        <#if generateToolbar>
        toolbar = view.findViewById(R.id.toolbar)
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        placeHolderView = view.findViewById(R.id.placeholder)
        </#if>
        <#if (screenType=='activity' && typeViewActivity=='3') || (screenType=='fragment' && typeViewFragment=='3')>
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        </#if>
        <#if generateRecyclerView>
        recyclerView = view.findViewById(R.id.recycler)
        </#if>
    }
    <#if generateToolbar>

    private fun initToolbar() {
        toolbar.title = null // todo поправить тайтл тулбара
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { _ -> presenter.finish() }
    }
    </#if>
    private fun initListeners() {
        <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
            placeHolderView.setOnActionClickListener { presenter.reloadData() }
        </#if>
        <#if (screenType=='activity' && typeViewActivity=='3') || (screenType=='fragment' && typeViewFragment=='3')>
            swipeRefreshLayout.setOnRefreshListener { presenter.reloadData() }
        </#if>
    }
    <#if generateRecyclerView>

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(<#if screenType=='activity'>this<#else>context</#if>)
        recyclerView.adapter = adapter
    }
    </#if>
     override fun getScreenName(): String = "${camelCaseToUnderscore(className)}"
}
