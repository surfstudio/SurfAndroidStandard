<#-- Макрос для определения, какой тип DialogRoute необходимо импортировать -->
<#macro importDialogRoute>
import ru.surfstudio.android.mvp.dialog.navigation.route.<#if typeRoute=='1'>DialogRoute<#else>DialogWithParamsRoute</#if>
</#macro>