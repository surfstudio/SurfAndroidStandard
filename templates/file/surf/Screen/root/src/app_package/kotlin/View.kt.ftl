package ${packageName}

import android.os.Bundle
<#if screenType=='activity'>
import android.os.PersistableBundle
</#if>
import android.view.View
import ru.surfstudio.android.core.mvp.${screenType}.${viewParentClassName}
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
<#if applicationPackage??>
import ${applicationPackage}.base_ui.component.provider.ComponentProvider
</#if>
import javax.inject.Inject
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Вью TODO
 */
class ${viewClassName} : ${viewParentClassName}() {

    @Inject
    lateinit var presenter: ${presenterClassName}

    override fun getScreenName() = "${viewClassName}"

    override fun getPresenters() = arrayOf(presenter)

    <#if screenType=='activity'>
    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)
    <#else>
    override fun createConfigurator() = ComponentProvider.createFragmentScreenConfigurator(arguments ?: bundleOf(), this::class)
    </#if>
    <#if screenType=='activity'>

    override fun getContentView(): Int = R.layout.${layoutName}
    </#if>
    <#if needToGenerateLds>

    override fun getPlaceHolderView() = TODO()
    </#if>
    <#if needToGenerateSwr>

    override fun getSwipeRefreshLayout() = TODO()
    </#if>
    <#if screenType=='fragment'>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.${layoutName}, container, false)
    </#if>

    <#if screenType=='activity'>
    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
    <#else>
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
    </#if>
        initListeners()
    }

    override fun renderInternal(screenModel: ${screenModelClassName}) {

    }

    private fun initListeners() {

    }
}