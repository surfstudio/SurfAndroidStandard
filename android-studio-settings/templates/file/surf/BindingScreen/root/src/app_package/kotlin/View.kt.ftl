package ${packageName}

import android.os.Bundle
import androidx.core.os.bundleOf
<#if screenType=='activity'>
import android.os.PersistableBundle
</#if>
import android.view.View
import ru.surfstudio.android.core.mvp.binding.rx.ui.${viewParentClassName}
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Вью TODO
 */
class ${viewClassName} : ${viewParentClassName}() {

    @Inject
    lateinit var bm: ${bindModelClassName}

    override fun getScreenName() = "${viewClassName}"
    <#if screenType=='fragment'>

    override fun getPresenters() = arrayOf(presenter)
    </#if>

    <#if screenType=='activity'>
    override fun createConfigurator() = ${configuratorClassName}(intent)
    <#else>
    override fun createConfigurator() = ${configuratorClassName}(arguments ?: bundleOf())
    </#if>
    <#if screenType=='activity'>

    override fun getContentView(): Int = R.layout.${layoutName}
    </#if>
    <#if screenType=='fragment'>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.${layoutName}, container, false)
    </#if>
     <#if screenType=='fragment'>

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews()
        initViews()
     }
     </#if>

    <#if screenType=='activity'>
    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
    findViews()
    initViews()
    <#else>
    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
    </#if>
        initListeners()
        bind()
    }

    private fun findViews() {}

    private fun initViews() {}

    private fun initListeners() {}

    private fun bind() {}
}