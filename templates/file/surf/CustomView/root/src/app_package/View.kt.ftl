package ${packageName};

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


class ${className}(context: Context, attrs: AttributeSet?) : ${parentClassName}(context, attrs) {

    <#if generateListener>
    private var listener: ((Unit) -> Unit)? = null
    </#if>

    init {
        initView()
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

    private fun initView() {
        inflate(context, R.layout.${layoutName}, this)
    }

}
