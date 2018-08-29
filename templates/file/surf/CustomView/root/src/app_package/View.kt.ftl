<#import "macros/import_r_class_macros.ftl" as importRMacros>

package ${packageName};

import android.content.Context
import android.util.AttributeSet
import android.view.View
<@importRMacros.importRClass />

class ${className} @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null
): ${parentClassName}(context, attrs) {
    <#if generateListener>

    private var listener: ((Unit) -> Unit)? = null
    </#if>

    init {
        View.inflate(context, R.layout.${layoutName}, this)
    }
    <#if generateListener>

        fun setListener(listener: ((Unit) -> Unit)?) {
            this.listener = listener
        }
    </#if>
    <#if screenModelName!=''>

        fun render(screenModel: ${screenModelName}) {
        }
    </#if>
}
