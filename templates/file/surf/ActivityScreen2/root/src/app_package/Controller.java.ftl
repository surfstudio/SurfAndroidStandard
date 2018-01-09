<#import "macros/select_type_controller_macros.ftl" as controllerMacros>
package ${packageName}.${folderController};

<#if applicationPackage??>
<#if typeController=='1'>
import ${applicationPackage}.ui.common.widget.easyadapter.holder.BindableViewHolder;
<#else>
import ${applicationPackage}.ui.common.widget.easyadapter.holder.BaseViewHolder;
</#if>
</#if>

public class ${nameController}${defPostfixController} extends <@controllerMacros.selectTypeControlle /> {

    private Listener listener;

    

    public ${nameController}${defPostfixController}(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Holder createViewHolder(ViewGroup parent) {
        return new Holder(parent);
    }

    <#if typeController=='1'>
    @Override
    public long getItemId(${nameTypeData} ${nameParam}) {
        return ${nameParam}.getId();
    }
    </#if>

    public interface Listener {
        void onItemClick(${nameTypeData} item);
    }

    class Holder extends <#if typeController=='1'>BindableViewHolder<${nameTypeData}><#else>BaseViewHolder</#if> {

        Holder(ViewGroup parent) {
           super(parent, R.layout.${nameRes});
        }

        <#if typeController=='1'>
        @Override
        public void bind(${nameTypeData} ${nameParam}) {
           
        }
        </#if>
    }
}
