package ${packageName}

import android.view.ViewGroup
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import ru.surfstudio.android.easyadapter.controller.<#if controllerType='bindable'>Bindable<#else>NoData</#if>ItemController
import ru.surfstudio.android.easyadapter.holder.<#if controllerType='bindable'>Bindable<#else>Base</#if>ViewHolder

/**
 * Контроллер TODO
 */
class ${controllerClassName}<#if needToGenerateListener>(
        private val onItemClickAction: (<#if controllerType='bindable'>${controllerItemClassName}</#if>) -> Unit
)</#if> : <#if controllerType='bindable'>Bindable<#else>NoData</#if>ItemController<<#if controllerType='bindable'>${controllerItemClassName}, </#if><#if controllerType='bindable' || needToGenerateListener>${controllerClassName}.Holder<#else>BaseViewHolder</#if>>() {

    override fun createViewHolder(parent: ViewGroup) = <#if controllerType='bindable' || needToGenerateListener>Holder(parent)<#else>BaseViewHolder(parent, R.layout.${layoutName})</#if>
    <#if controllerType='bindable'>

    override fun getItemId(${controllerItemName}: ${controllerItemClassName}) = TODO()
    </#if>
    <#if controllerType='bindable' || needToGenerateListener>

    inner class Holder(parent: ViewGroup) : <#if controllerType='bindable'>Bindable<#else>Base</#if>ViewHolder<#if controllerType='bindable'><${controllerItemClassName}></#if>(parent, R.layout.${layoutName}) {
        <#if needToGenerateListener>
        <#if controllerType='bindable'>

        private lateinit var ${controllerItemName}: ${controllerItemClassName}
        </#if>

        init {
            itemView.setOnClickListener { onItemClickAction(<#if controllerType='bindable'>${controllerItemName}</#if>) }
        }
        </#if>
        <#if controllerType='bindable'>

        override fun bind(${controllerItemName}: ${controllerItemClassName}) {<#if needToGenerateListener>
            this.${controllerItemName} = ${controllerItemName}</#if>

        }
        </#if>
    }
    </#if>
}