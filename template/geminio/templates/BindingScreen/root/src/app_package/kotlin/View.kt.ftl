<#if (fragment)>
    <#assign screenType = "Fragment">
<#else>
    <#assign screenType = "Activity">
</#if>

package ${packageName}

import android.os.Bundle
import androidx.core.os.bundleOf
<#if (activity)>
import android.os.PersistableBundle
</#if>
import android.view.View
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRx${screenType?cap_first}View
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import javax.inject.Inject
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Вью TODO
 */
class ${viewClassName} : BaseRx${screenType?cap_first}View() {

    @Inject
    lateinit var bm: ${bindModelClassName}

    override fun getScreenName() = "${viewClassName}"
    <#if (fragment)>

    override fun getPresenters() = arrayOf(presenter)
    </#if>

    <#if (activity)>
    override fun createConfigurator() = ${configuratorClassName}(intent)
    <#else>
    override fun createConfigurator() = ${configuratorClassName}(arguments ?: bundleOf())
    </#if>
    <#if (activity)>

    override fun getContentView(): Int = R.layout.${layoutName}
    </#if>
    <#if (fragment)>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.${layoutName}, container, false)
    </#if>
     <#if (fragment)>

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews()
        initViews()
     }
     </#if>

    <#if (activity)>
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