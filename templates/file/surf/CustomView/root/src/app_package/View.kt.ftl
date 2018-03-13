package ${packageName};

import android.content.Context;
import android.util.AttributeSet;

class ${className} @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ${parentClassName}(context, attrs) {

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
