<#import "macros/select_type_activity_route_macros.ftl" as superClass>
package ${packageName};

import android.content.Context;
import android.content.Intent;

<#if typeActivityRoute=='2' || typeActivityRoute=='4'>
import lombok.Data;
</#if>
import ${applicationPackage}.ui.base.navigation.activity.route.<@superClass.selectTypeRouteForImport />

/**
* Маршрут экрана {@link ${className}ActivityView}
*/
<#if typeActivityRoute=='2' || typeActivityRoute=='4'>
@Data
</#if>
public class ${className}${defPostfixActivityRoute} extends <@superClass.selectTypeRoute /> {

	<#if typeActivityRoute=='3' || typeActivityRoute=='4'>
    public ${className}${defPostfixActivityRoute}() {
        // пустой конструктор для ActivityNavigator.observeResult()
    }
    </#if>

	<#if typeActivityRoute=='2' || typeActivityRoute=='4'>
    public ${className}${defPostfixActivityRoute}(Intent intent) {
        
    }
    </#if>

    @Override
    public Intent prepareIntent(Context context) {
        Intent intent = new Intent(context, ${className}${defPostfixActivityRoute}.class);
        return intent;
    }
}
