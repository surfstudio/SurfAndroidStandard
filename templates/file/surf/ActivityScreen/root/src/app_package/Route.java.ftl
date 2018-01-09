<#import "macros/select_type_route_macros.ftl" as superClass>
package ${packageName};


public class ${className}${screenTypeCapitalized}Route extends <@superClass.selectTypeRoute /> {

    <#if screenType=='activity'>
        <#if typeRouteActivity=='2' || typeRouteActivity=='4'>
    ${className}ActivityRoute(Intent intent) {

    }
        </#if>

    @Override
    public Intent prepareIntent(Context context) {
        Intent intent = new Intent(context, ${className}${screenTypeCapitalized}View.class);

        return intent;
    }
    <#else>
        <#if typeRouteFragment=='2'>
    ${className}FragmentRoute(Bundle argst) {

    }
        </#if>
    
    @Override
    public Bundle prepareBundle() {
        Bundle args = new Bundle();

        return args;
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return ${className}${screenTypeCapitalized}View.class;
    }
    </#if>

}
