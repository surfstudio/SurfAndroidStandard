<#import "macros/select_type_dialog_route_macros.ftl" as superClass>
package ${packageName};

<#if typeDialogRoute=='2'>
import android.os.Bundle;
</#if>

<#if typeDialogRoute=='2'>
import lombok.Data;
</#if>
import ${applicationPackage}.ui.base.navigation.dialog.route.<@superClass.selectTypeRouteForImport/>
import ${applicationPackage}.ui.base.screen.dialog.BaseDialogFragment;

/**
* Маршрут диалога {@link ${className}Dialog}
*/
<#if typeDialogRoute=='2'>
@Data
</#if>
public class ${className}${defPostfixDialogRoute} extends <@superClass.selectTypeRoute /> {

    @Override
    protected Class<? extends BaseDialogFragment> getFragmentClass() {
        return ${className}Dialog.class;
    }

    <#if typeDialogRoute=='2'>
    @Override
    protected Bundle prepareBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }
    </#if>
}