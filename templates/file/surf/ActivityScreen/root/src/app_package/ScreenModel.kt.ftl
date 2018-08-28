<#import "macros/select_type_screen_model_macros.ftl" as superClass>
package ${packageName}

import ru.surfstudio.android.core.mvp.model.LdsScreenModel
import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel
import ru.surfstudio.android.core.mvp.model.ScreenModel

class ${className}ScreenModel : <@superClass.selectTypeScreenModel /> {
    <#if generateRecyclerView>
        <#if nameTypeData==''>
            <#assign nameTypeData='Unit' />
        </#if>
        <#if usePaginationableAdapter>
            var itemList: DataList<${nameTypeData}> = DataList.empty()
        <#else>
            var itemList: List<${nameTypeData}> = Collections.emptyList()
        </#if>
    </#if>
}
