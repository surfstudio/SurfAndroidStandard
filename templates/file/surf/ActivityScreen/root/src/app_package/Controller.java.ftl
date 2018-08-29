<#import "macros/controller/select_import_type_controller_macros.ftl" as importControllerMacros>
<#import "macros/controller/select_import_type_holder_macros.ftl" as importHolderMacros>
<#import "macros/controller/select_type_controller_macros.ftl" as controllerMacros>
<#import "macros/controller/select_type_holder_macros.ftl" as holderMacros>
<#import "macros/controller/import_r_class_macros.ftl" as importRMacros>

package ${packageName};

import android.view.ViewGroup;
<@importRMacros.importRClass />;
import ru.surfstudio.android.easyadapter.controller.<@importControllerMacros.selectImportTypeController />;
import ru.surfstudio.android.easyadapter.holder.<@importHolderMacros.selectImportTypeHolder />;

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
    public String getItemId(${nameTypeData} ${nameParam}) {
        return String.valueOf(${nameParam}.getId());
    }

    </#if>
    class Holder extends <@holderMacros.selectTypeHolder /> {

        Holder(ViewGroup parent) {
           super(parent, R.layout.${nameRes});
           <#if hasListener && typeController='2'>
           itemView.setOnClickListener(v -> listener.onItemClick());
           </#if>
           //todo find view here
        }
        <#if typeController='1'>
        @Override
        public void bind(${nameTypeData} ${nameParam}) {
            <#if hasListener>
           itemView.setOnClickListener(v -> listener.onItemClick(${nameParam}));
            </#if>
           //todo render data here
        }
        </#if>
    }
}
