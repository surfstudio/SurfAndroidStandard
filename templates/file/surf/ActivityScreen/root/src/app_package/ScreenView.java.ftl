<#import "macros/select_type_view_macros.ftl" as superClass>
<#import "macros/fragment_view_macros.ftl" as fragmentView>
<#import "function/lds_view_function.ftl" as ldsFunction>
<#import "function/lds_swr_view_function.ftl" as ldsSwrFunction>

package ${packageName};

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView;
import ru.surfstudio.android.core.mvp.activity.BaseLdsSwrActivityView;
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView;
import ru.surfstudio.android.core.mvp.fragment.BaseLdsFragmentView;
import ru.surfstudio.android.core.mvp.fragment.BaseLdsSwrFragmentView;
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView;
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.easyadapter.EasyAdapter;
import ru.surfstudio.android.easyadapter.ItemList;
import ru.surfstudio.standard.base_ui.recylcer.adapter.PaginationableAdapter;
<#if applicationPackage??>import ${applicationPackage}.R;</#if>
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator;
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator;
import ru.surfstudio.standard.ui.base.placeholder.PlaceHolderView;
import javax.inject.Inject;

import android.support.annotation.Nullable;
<#if generateToolbar>
import android.support.v7.widget.Toolbar;
</#if>
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Вью экрана todo
 */
public class ${className}${screenTypeCapitalized}View extends <@superClass.selectTypeView /> {

    @Inject
    ${className}Presenter presenter;
    <#if generateToolbar>

    private Toolbar toolbar;
    </#if>
    <#if ldsFunction.isLdsView()>

    private PlaceHolderViewImpl placeHolderView;
    </#if>
    <#if ldsSwrFunction.isLdsSwrView()>

    private SwipeRefreshLayout swipeRefreshLayout;
    </#if>
    <#if generateRecyclerView>

    private RecyclerView recyclerView;
        <#if usePaginationableAdapter>

        private PaginationableAdapter adapter;
        <#else>

        private EasyAdapter adapter;
        </#if>

    private ${nameController}${defPostfixController} ${nameController?uncap_first}${defPostfixController};
    </#if>
    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }
    <#if screenType=='activity'>

        @Override
        public BaseActivityViewConfigurator createConfigurator() {
            return new ${className}ScreenConfigurator(this.getIntent());
        }

        @Override
        public int getContentView() {
            return R.layout.${layoutName};
        }
    <#else>

        @Override
        public BaseFragmentViewConfigurator createConfigurator() {
            return new ${className}ScreenConfigurator(this.getArguments());
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.${layoutName}, container, false);
        }
    </#if>
    <#if screenType=='activity'>

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState,
                         boolean viewRecreated) {
        findViews();
    <#else>
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {
         findViews(getView());
   </#if>
        <#if generateToolbar>
        initToolbar();
        </#if>
        initListeners();
        <#if generateRecyclerView>
        initRecyclerView();
        </#if>
    }
    <#if ldsFunction.isLdsView()>

    @Override
    protected PlaceHolderView getPlaceHolderView() {
        return placeHolderView;
    }
    </#if>
    <#if ldsSwrFunction.isLdsSwrView()>

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }
    </#if>

    @Override
    protected void renderInternal(${className}ScreenModel screenModel) {
    <#if generateRecyclerView>
        <#if usePaginationableAdapter>
             adapter.setItems(ItemList.create()
                  .addAll(screenModel.getItemList(), ${nameController?uncap_first}${defPostfixController}), screenModel.getPaginationState());
        <#else>
              adapter.setItems(ItemList.create()
                  .addAll(screenModel.getItemList(), ${nameController?uncap_first}${defPostfixController}));
        </#if>
    </#if>
    }

    private void findViews(<#if screenType=='fragment'>View view</#if>) {
        <#if generateToolbar>
        toolbar = <@fragmentView.fragmentView/>findViewById(R.id.toolbar);
        </#if>
        <#if ldsFunction.isLdsView()>
        placeHolderView = <@fragmentView.fragmentView/>findViewById(R.id.placeholder);
        </#if>
        <#if ldsSwrFunction.isLdsSwrView()>
        swipeRefreshLayout = <@fragmentView.fragmentView/>findViewById(R.id.swipe_refresh);
        </#if>
        <#if generateRecyclerView>
        recyclerView = <@fragmentView.fragmentView/>findViewById(R.id.recycler);
        </#if>
    }
    <#if generateToolbar>

    private void initToolbar() {
        toolbar.setTitle(null); // todo поправить тайтл тулбара
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> presenter.finish());
    }
    </#if>
    private void initListeners() {
        <#if ldsFunction.isLdsView()>
            placeHolderView.setOnActionClickListener(state -> {
                presenter.reloadData();
                return null;
            });
        </#if>
        <#if ldsSwrFunction.isLdsSwrView()>
            swipeRefreshLayout.setOnRefreshListener(() -> presenter.reloadData());
        </#if>
    }
    <#if generateRecyclerView>

    private void initRecyclerView() {
        ${nameController?uncap_first}${defPostfixController} = new ${nameController}${defPostfixController}(<#if hasListener>presenter::on${nameTypeData}ItemClick</#if>);

        <#if usePaginationableAdapter>
        adapter = new PaginationableAdapter();
        adapter.setOnShowMoreListener(() -> presenter.loadMore());
        <#else>
        adapter = new EasyAdapter();
        </#if>

        recyclerView.setLayoutManager(new LinearLayoutManager(<#if screenType=='activity'>this<#else>getContext()</#if>));
        recyclerView.setAdapter(adapter);
    }
    </#if>
    @Override
    public String getName() {
        return "${camelCaseToUnderscore(className)}";
    }
}
