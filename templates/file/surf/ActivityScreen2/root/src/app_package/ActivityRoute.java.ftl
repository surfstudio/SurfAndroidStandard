<#import "macros/select_type_route_macros.ftl" as superClass>
package ${packageName};

@Data
public class ${className}ActivityRoute extends <@superClass.selectTypeRoute /> {

	<#if typeRoute=='2' || typeRoute=='4'>
    public ${className}${defPostfixRoute}(Intent intent) {
        
    }
    </#if>

    @Override
    public Intent prepareIntent(Context context) {
        Intent intent = new Intent(context, ${className}${defPostfixView}.class);

        return intent;
    }
}
