<#import "macros/select_import_type_controller_macros.ftl" as importControllerMacros>
<#import "macros/select_import_type_holder_macros.ftl" as importHolderMacros>
<#import "macros/select_type_controller_macros.ftl" as controllerMacros>
<#import "macros/select_type_holder_macros.ftl" as holderMacros>

package ${packageName}

import android.view.ViewGroup
<#if applicationPackage??>import ${applicationPackage}.R</#if>
import ru.surfstudio.android.easyadapter.controller.<@importControllerMacros.selectImportTypeController />
import ru.surfstudio.android.easyadapter.holder.<@importHolderMacros.selectImportTypeHolder />

class ${nameController}${defPostfixController}(<#if hasListener>private val onClickListener: (<#if typeController='1'>${nameParam}: ${nameTypeData} </#if>) -> Unit
</#if>) : <@controllerMacros.selectTypeController />() {
    
    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    <#if typeController='1'>
    override fun getItemId(${nameParam}: ${nameTypeData}): String = ${nameParam}.id.toString()
    
    </#if>
    inner class Holder(parent: ViewGroup) : <@holderMacros.selectTypeHolder />(parent, R.layout.${nameRes}) {

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
