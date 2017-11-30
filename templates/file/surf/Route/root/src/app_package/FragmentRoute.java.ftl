<#import "macros/select_type_fragment_route_macros.ftl" as superClass>
package ${packageName};

<#if typeFragmentRoute=='2'>
import android.os.Bundle;
</#if>
import android.support.v4.app.Fragment;

<#if typeFragmentRoute=='2'>
import lombok.Data;
</#if>
import ${applicationPackage}.ui.base.navigation.fragment.route.<@superClass.selectTypeRouteForImport />

/**
* Маршрут экрана {@link ${className}FragmentView}
*/
<#if typeFragmentRoute=='2'>
@Data
</#if>
public class ${className}${defPostfixFragmentRoute} extends <@superClass.selectTypeRoute /> {

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return ${className}FragmentView.class;
    }

    <#if typeFragmentRoute=='2'>
    @Override
    protected Bundle prepareBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }
    </#if>
}