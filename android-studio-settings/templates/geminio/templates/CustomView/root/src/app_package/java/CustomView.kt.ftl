package ${packageName};

import android.content.Context
import android.util.AttributeSet
import android.view.View
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>

/**
 * TODO
 */
class ${customViewClassName} @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
): ${parentViewClassName}(context, attrs) {
    <#if needToGenerateLayout>

    init {
        View.inflate(context, R.layout.${layoutName}, this)
    }
    </#if>
}