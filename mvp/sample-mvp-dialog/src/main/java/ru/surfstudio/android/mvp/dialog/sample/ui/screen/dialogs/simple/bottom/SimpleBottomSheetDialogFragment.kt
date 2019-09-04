package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ru.surfstudio.android.mvp.dialog.sample.R
import ru.surfstudio.android.mvp.dialog.simple.bottomsheet.CoreSimpleBottomSheetDialogFragment
import javax.inject.Inject

class SimpleBottomSheetDialogFragment : CoreSimpleBottomSheetDialogFragment() {

    @Inject lateinit var presenter: SimpleBottomSheetDialogPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getScreenComponent(SimpleBottomSheetDialogComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.simple_bottom_sheet_dialog_layout, container)
        view.findViewById<LinearLayout>(R.id.simple_bottom_sheet_dialog_action_container).apply {
            setOnClickListener {
                presenter.simpleBottomSheetDialogSuccessAction()
                dismiss()
            }
        }
        return view
    }

    override fun getName(): String = "Simple Bottom Sheet Dialog Fragment"
}