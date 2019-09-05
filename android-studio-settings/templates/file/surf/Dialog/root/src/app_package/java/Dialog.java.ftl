package ${packageName};

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>

import javax.inject.Inject;

import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment;

/**
 * TODO
 */
public class ${dialogClassName} extends CoreSimpleDialogFragment {

    @Inject
    ${screenClassNameWithoutPostfix}Presenter presenter;
    @Inject
    ${dialogRouteClassName} route;

    @Override
    public String getName() {
        return "${dialogClassName}";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenComponent(${screenClassNameWithoutPostfix}ScreenComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.${layoutName}, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
    }

    private void initViews(View view) {

    }

    private void initListeners() {

    }
}
