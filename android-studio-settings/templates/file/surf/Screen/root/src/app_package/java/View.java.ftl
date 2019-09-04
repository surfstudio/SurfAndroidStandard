package ${packageName};

import android.os.Bundle;
<#if screenType=='activity'>
import android.os.PersistableBundle;
</#if>
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import ru.surfstudio.android.core.mvp.${screenType}.${viewParentClassName};
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
<#if applicationPackage??>
import ${applicationPackage}.R;
import ${applicationPackage}.base_ui.component.provider.ComponentProvider
</#if>
import ru.surfstudio.standard.ui.base.placeholder.PlaceHolderView;
import javax.inject.Inject;

import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Вью TODO
 */
public class ${viewClassName} extends ${viewParentClassName} {

    @Inject
    ${presenterClassName} presenter;

    @Override
    public String getScreenName() {
        return "${viewClassName}";
    }

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    <#if screenType=='activity'>
    @Override
    public BaseActivityViewConfigurator createConfigurator() {
        return ComponentProvider.createActivityScreenConfigurator(intent, this.class);
    }
    <#else>
    @Override
    public BaseFragmentViewConfigurator createConfigurator() {
        return ComponentProvider.createFragmentScreenConfigurator(arguments, this.class)
    }
    </#if>
    <#if screenType=='activity'>

    @Override
    public int getContentView() {
        return R.layout.${layoutName};
    }
    </#if>
    <#if needToGenerateLds>

    @Override
    protected PlaceHolderView getPlaceHolderView() {
        //TODO
    }
    </#if>
    <#if needToGenerateSwr>

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        //TODO
    }
    </#if>
    <#if screenType=='fragment'>

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.${layoutName}, container, false);
    }
    </#if>

    <#if screenType=='activity'>
    @Override
    public void onCreate(
            @Nullable Bundle savedInstanceState,
            @Nullable PersistableBundle persistentState,
            boolean viewRecreated
    ) {
        findViews();
    <#else>
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {
        findViews(getView());
    </#if>
        initListeners();
    }

    @Override
    protected void renderInternal(${screenModelClassName} screenModel) {

    }

    private void findViews(<#if screenType=='fragment'>View view</#if>) {

    }

    private void initListeners() {

    }
}
