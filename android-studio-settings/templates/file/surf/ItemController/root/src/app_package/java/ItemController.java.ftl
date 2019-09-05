package ${packageName};

import android.view.ViewGroup;
<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>

import ru.surfstudio.android.easyadapter.controller.<#if controllerType='bindable'>Bindable<#else>NoData</#if>ItemController;
import ru.surfstudio.android.easyadapter.holder.<#if controllerType='bindable'>Bindable<#else>Base</#if>ViewHolder;

/**
 * Контроллер TODO
 */
public class ${controllerClassName} extends <#if controllerType='bindable'>Bindable<#else>NoData</#if>ItemController<<#if controllerType='bindable'>${controllerItemClassName}, </#if><#if controllerType='bindable' || needToGenerateListener>${controllerClassName}.Holder<#else>BaseViewHolder</#if>> {
    <#if needToGenerateListener>

    private final OnItemClickListener onItemClickListener;

    public ${controllerClassName}(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    </#if>

    @Override
    public Holder createViewHolder(ViewGroup parent) {
        <#if controllerType='bindable' || needToGenerateListener>return new Holder(parent)<#else>return new BaseViewHolder(parent, R.layout.${layoutName})</#if>;
    }
    <#if controllerType='bindable'>

    @Override
    public String getItemId(${controllerItemClassName} ${controllerItemName}) {
        //TODO
    }
    </#if>
    <#if needToGenerateListener>

    public interface OnItemClickListener {

        void onItemClick(<#if controllerType='bindable'>${controllerItemClassName} ${controllerItemName}</#if>);
    }
    </#if>
    <#if controllerType='bindable' || needToGenerateListener>

    class Holder extends <#if controllerType='bindable'>Bindable<#else>Base</#if>ViewHolder<#if controllerType='bindable'><${controllerItemClassName}></#if> {
        <#if controllerType='bindable' && needToGenerateListener>

        private ${controllerItemClassName} ${controllerItemName};
        </#if>

        Holder(ViewGroup parent) {
            super(parent, R.layout.${layoutName});<#if needToGenerateListener>
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(<#if controllerType='bindable'>${controllerItemName}</#if>));</#if>
        }
        <#if controllerType='bindable'>

        @Override
        public void bind(${controllerItemClassName} ${controllerItemName}) {<#if needToGenerateListener>
            this.${controllerItemName} = ${controllerItemName};</#if>

        }
        </#if>
    }
    </#if>
}
