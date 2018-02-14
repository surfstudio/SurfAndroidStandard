package ${packageName};

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject


class ${className}Dialog : BaseSimpleDialogFragment() {
    @Inject
    lateinit var presenter: ${screenName}Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getScreenComponent(${screenName}ScreenComponent.class).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                             container: ViewGroup?,
                             savedInstanceState: Bundle?) : View? {
        return inflater.inflate(R.layout.${layoutName}, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        initListeners()
    }

    override fun getName(): String = "${camelCaseToUnderscore(className)}"

    private fun findViews(view: View) {
    }

    private fun initListeners() {
    }

}
