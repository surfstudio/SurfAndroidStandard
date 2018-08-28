<#import "macros/controller/select_import_type_controller_macros.ftl" as importControllerMacros>
<#import "macros/controller/select_import_type_holder_macros.ftl" as importHolderMacros>
<#import "macros/controller/select_type_controller_macros.ftl" as controllerMacros>
<#import "macros/controller/select_type_holder_macros.ftl" as holderMacros>
<#import "macros/controller/import_r_class_macros.ftl" as importRMacros>

package ${packageName}

import android.view.ViewGroup
<@importRMacros.importRClass />

import ru.surfstudio.android.easyadapter.controller.<@importControllerMacros.selectImportTypeController />
import ru.surfstudio.android.easyadapter.holder.<@importHolderMacros.selectImportTypeHolder />

class ${nameController}${defPostfixController}(<#if hasListener>private val onClickListener: (<#if typeController='1'>${nameParam}: ${nameTypeData} </#if>) -> Unit
</#if>) : <@controllerMacros.selectTypeController />() {
    
    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    <#if typeController='1'>
    override fun getItemId(${nameParam}: ${nameTypeData}): String = ${nameParam}.id.toString()
    
    </#if>
    inner class Holder(parent: ViewGroup) : <@holderMacros.selectTypeHolder />(parent, R.layout.${nameRes}) {

        init {
           <#if hasListener && typeController='2'>
           itemView.setOnClickListener { onClickListener.invoke() }
           </#if>
           //todo find view here
        }
        <#if typeController='1'>
        override fun bind(${nameParam}: ${nameTypeData}) {
            <#if hasListener>
           itemView.setOnClickListener { onClickListener.invoke(${nameParam}) }
            </#if>
           //todo render data
        }
        </#if>
    }
}
