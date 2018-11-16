package ${packageName};

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>

/**
 * TODO
 */
public class ${customViewClassName} extends ${parentViewClassName} {

    public ${customViewClassName}(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        <#if needToGenerateLayout>
        initView();
        </#if>
    }
    <#if needToGenerateScreenModel>

    public void render(${screenModelClassName} screenModel) {

    }
    </#if>
    <#if needToGenerateLayout>

    private void initView() {
        inflate(getContext(), R.layout.${layoutName}, this);
    }
    </#if>
}
