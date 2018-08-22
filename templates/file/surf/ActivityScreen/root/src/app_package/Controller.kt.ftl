<#import "macros/select_type_controller_macros.ftl" as controllerMacros>
package ${packageName}

import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.standard.R

class ${nameController}${defPostfixController}(<#if hasListener>
        private val onClickListener: (<#if typeController='1'>${nameParam}: ${nameTypeData} </#if>) -> Unit
</#if>) : <@controllerMacros.selectTypeController />() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    <#if typeController='1'>
    override fun getItemId(${nameParam}: ${nameTypeData}): String = ${nameParam}.id.toString()

    </#if>
    inner class Holder(parent: ViewGroup) : <#if typeController='1'>BindableViewHolder<${nameTypeData}><#else>BaseViewHolder</#if>(parent, R.layout.${nameRes}) {

        <#if typeController='1'>
        private lateinit var ${nameParam}: ${nameTypeData}

        </#if>
        init {
           <#if hasListener>
           itemView.setOnClickListener { onClickListener.invoke(<#if typeController='1'>${nameParam}</#if>) }
           </#if>
           //todo find view here
        }

        <#if typeController='1'>
        override fun bind(${nameParam}: ${nameTypeData}) {
           this.${nameParam} = ${nameParam}
           //todo render data
        }
        </#if>
    }
}
