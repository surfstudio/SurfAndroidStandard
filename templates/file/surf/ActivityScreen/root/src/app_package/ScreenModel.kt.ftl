<#import "macros/select_type_screen_model_macros.ftl" as superClass>
package ${packageName}


class ${className}ScreenModel : <@superClass.selectTypeScreenModel /> {
    <#if generateRecyclerView>
        <#if nameTypeData==''>
            <#assign nameTypeData='Unit' />
        </#if>
        <#if (screenType=='activity' && usePaginationableAdapter) || (screenType=='fragment' && usePaginationableAdapter)>
            var itemList: DataList<${nameTypeData}> = DataList.empty()
        <#else>
            var itemList: List<${nameTypeData}> = Collections.emptyList()
        </#if>
    </#if>
}
