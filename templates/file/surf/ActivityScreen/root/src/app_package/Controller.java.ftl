<#import "macros/select_type_controller_macros.ftl" as controllerMacros>
package ${packageName};

public class ${nameController}${defPostfixController} extends <@controllerMacros.selectTypeController /> {

    <#if hasListener>
    private Listener listener;

    public interface Listener {
        void onItemClick(<#if typeController='1'>${nameTypeData} ${nameParam}</#if>);
    }

    public ${nameController}${defPostfixController}(Listener listener) {
        this.listener = listener;
    }
    
    </#if>
    @Override
    public Holder createViewHolder(ViewGroup parent) {
        return new Holder(parent);
    }

    <#if typeController='1'>
    @Override
    public long getItemId(${nameTypeData} ${nameParam}) {
        return ${nameParam}.getId();
    }
    
    </#if>
    class Holder extends <#if typeController='1'>BindableViewHolder<${nameTypeData}><#else>BaseViewHolder</#if> {

        <#if typeController='1'>
        private ${nameTypeData} ${nameParam};
        
        </#if>
        Holder(ViewGroup parent) {
           super(parent, R.layout.${nameRes});
           <#if hasListener>
           itemView.setOnClickListener(v -> listener.onItemClick(<#if typeController='1'>${nameParam}</#if>));
           </#if>
           //todo find view here
        }

        <#if typeController='1'>
        @Override
        public void bind(${nameTypeData} ${nameParam}) {
           this.${nameParam} = ${nameParam};
           //todo render data here
        }
        </#if>
    }
}
