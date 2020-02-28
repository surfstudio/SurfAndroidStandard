package ${packageName};

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment
import javax.inject.Inject

/**
 * TODO
 */
class ${dialogClassName} : CoreSimpleDialogFragment() {

    @Inject
    lateinit var presenter: ${screenClassNameWithoutPostfix}Presenter
    @Inject
    lateinit var route: ${dialogRouteClassName}

    override fun getName() = "${dialogClassName}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getScreenComponent(${screenClassNameWithoutPostfix}ScreenComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.${layoutName}, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {

    }
}