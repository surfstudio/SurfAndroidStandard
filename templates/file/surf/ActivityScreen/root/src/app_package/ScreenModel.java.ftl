<#import "macros/select_type_screen_model_macros.ftl" as superClass>
package ${packageName};

@Data
class ${className}ScreenModel extends <@superClass.selectTypeScreenModel /> {
    <#if generateRecyclerView>
            <#if (screenType=='activity' && typeViewActivity=='5') || (screenType=='fragment' && typeViewFragment=='5')>
                private DataList<${nameTypeData}> itemList = DataList.empty();
            <#else>
                private List<${nameTypeData}> itemList = Collections.emptyList();
            </#if>
    </#if>
}
