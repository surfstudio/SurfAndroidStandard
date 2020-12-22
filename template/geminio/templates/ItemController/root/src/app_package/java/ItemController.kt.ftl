package ${packageName}

<#assign controllerClassName = "${controllerClassNameWithoutPostfix}ItemController">
<#assign controllerItemName = "${controllerItemClassName?uncap_first}">

import android.view.ViewGroup
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import ru.surfstudio.android.easyadapter.controller.<#if bindable>Bindable<#else>NoData</#if>ItemController
import ru.surfstudio.android.easyadapter.holder.<#if bindable>Bindable<#else>Base</#if>ViewHolder

/**
 * Контроллер TODO
 */
class ${controllerClassName}<#if needToGenerateListener>(
        private val onItemClickAction: (<#if bindable>${controllerItemClassName}</#if>) -> Unit
)</#if> : <#if bindable>Bindable<#else>NoData</#if>ItemController<<#if bindable>${controllerItemClassName}, </#if><#if bindable || needToGenerateListener>${controllerClassName}.Holder<#else>BaseViewHolder</#if>>() {

    override fun createViewHolder(parent: ViewGroup) = <#if bindable || needToGenerateListener>Holder(parent)<#else>BaseViewHolder(parent, R.layout.${layoutName})</#if>
    <#if bindable>

    override fun getItemId(${controllerItemName}: ${controllerItemClassName}) = TODO()
    </#if>
    <#if bindable || needToGenerateListener>

    inner class Holder(parent: ViewGroup) : <#if bindable>Bindable<#else>Base</#if>ViewHolder<#if bindable><${controllerItemClassName}></#if>(parent, R.layout.${layoutName}) {
        <#if needToGenerateListener>
        <#if bindable>

        private lateinit var ${controllerItemName}: ${controllerItemClassName}
        </#if>

        init {
            itemView.setOnClickListener { onItemClickAction(<#if bindable>${controllerItemName}</#if>) }
        }
        </#if>
        <#if bindable>

        override fun bind(${controllerItemName}: ${controllerItemClassName}) {<#if needToGenerateListener>
            this.${controllerItemName} = ${controllerItemName}</#if>

        }
        </#if>
    }
    </#if>
}