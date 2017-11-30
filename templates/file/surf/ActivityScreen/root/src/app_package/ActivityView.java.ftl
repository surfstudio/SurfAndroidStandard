<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName};

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
<#if isUseToolbar>
import android.support.v7.widget.Toolbar;
</#if>

public class ${className}${defPostfixView} extends <@superClass.selectTypeView /> {

    @Inject
    ${className}${defPostfixPresenter} presenter;

    <#if isUseToolbar>
    private Toolbar toolbar;
    </#if>
    <#if typeView=='3' || typeView=='4' || typeView=='5'>
    private PlaceHolderView placeHolderView;
    </#if>
    <#if typeView=='4' || typeView=='5'>
    private SwipeRefreshLayout swipeRefreshLayout;
    </#if>
    <#if isUseRecyclerView>
    private RecyclerView recyclerView;
    private EasyAdapter adapter;
    </#if>
    <#if isUseController>
    ${nameController}${defPostfixController} itemController;
    </#if>

    <#if typeView=='5'>
    @Override
    public void notifyListChanged(ListChanges listChanges) {
       //todo notify adapter
    }
    </#if>

    @Override
    public int getContentView() {
        return R.layout.${layoutName};
    }

    <#if typeView=='3' || typeView=='4' || typeView=='5'>
    @Override
    protected PlaceHolderView getPlaceHolderView() {
        return placeHolderView;
    }
    </#if>

    <#if typeView=='4' || typeView=='5'>
    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }
    </#if>

    <#if typeView=='5'>
    @Override
    protected PaginationableAdapter getPaginationableAdapter() {
        return null;
    }
    </#if>
    
    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    @Override
    public ScreenConfigurator createScreenConfigurator(Context context, Intent intent) {
        return new ${className}${defPostfixScreenConfigurator}(context, intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated);
        findViews();
        initListeners();
        <#if isUseToolbar>
        initToolbar();
        </#if>
        <#if isUseRecyclerView>
        initRecycler();
        </#if>
    }

    <#if isUseScreenModel && typeView!='1'>
    @Override
    protected void renderInternal(${className}${defPostfixScreenModel} screenModel) {
         <#if isUseRecyclerView>
          ItemList items = ItemList.create()
                <#if isUseController>
                  <#if typeController=='1'>
                   .add(null, itemController);
                  </#if>
                  <#if typeController=='2'>
                   .add(itemController);
                  </#if>
                <#else>
                .add(null);// TODO add controller
                </#if>
          adapter.setItems(items);
         </#if>
         //todo render model here
    }
    </#if>

    private void findViews() {
        <#if isUseToolbar>
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        </#if>
        <#if typeView=='3' || typeView=='4' || typeView=='5'>
        placeHolderView = (PlaceHolderView) findViewById(R.id.placeholder);
        </#if>
	    <#if typeView=='4' || typeView=='5'>
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        </#if>
        <#if isUseRecyclerView>
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        </#if>
    }
    
    private void initListeners() {
        //todo init listeners here
    }

    <#if isUseToolbar>
    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> presenter.finish());
        //todo init toolbar here
    }
    </#if>

    <#if isUseRecyclerView>
    private void initRecycler() {
        <#if isUseController>
        itemController = new ${nameController}${defPostfixController}(data -> null); //todo create listener
        </#if>
        adapter = new EasyAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(${className}${defPostfixView}.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    </#if>
}
