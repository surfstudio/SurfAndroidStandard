package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.sample.common.R
import ru.surfstudio.android.sample.common.ui.base.loadstate.ErrorLoadState

/**
 * Presentation of ErrorLoadState with a picture, title, subtitle and button
 */
class ErrorLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        SimpleLoadStatePresentation<ErrorLoadState>() {

    @DrawableRes
    private var imageRes: Int = R.drawable.ic_error_state

    @StringRes
    private var titleRes: Int = R.string.placeholder_error_state_title_txt

    private var subtitleRes: Int = R.string.placeholder_error_state_subtitle_txt

    @StringRes
    private var btnRes: Int = R.string.placeholder_error_state_btn

    private lateinit var container: LinearLayout
    private lateinit var imageIv: ImageView
    private lateinit var titleTv: TextView
    private lateinit var subtitleTv: TextView
    private lateinit var actionBtn: Button

    private var onBtnClickedListener: () -> Unit = {}

    private val view: View by lazy {
        with(LayoutInflater.from(placeHolder.context).inflate(
                R.layout.placeholder_view_no_data_strategy,
                placeHolder,
                false)) {
            findViews(this)
            return@lazy this
        }
    }

    override fun showState(state: ErrorLoadState) {
        initViews(view)
        placeHolder.changeViewTo(view)
        placeHolder.show()
    }

    fun configState(imageRes: Int? = null,
                    titleRes: Int? = null,
                    subtitleRes: Int? = null,
                    btnRes: Int? = null,
                    onBtnClickedListener: (() -> Unit)? = null) {
        imageRes?.let { this.imageRes = it }
        titleRes?.let { this.titleRes = it }
        subtitleRes?.let { this.subtitleRes = it }
        btnRes?.let { this.btnRes = it }
        onBtnClickedListener?.let { this.onBtnClickedListener = it }
    }

    private fun findViews(view: View) {
        container = view.findViewById(R.id.placeholder_view_no_data_strategy_container)
        imageIv = view.findViewById(R.id.placeholder_image_iv)
        titleTv = view.findViewById(R.id.placeholder_title_tv)
        subtitleTv = view.findViewById(R.id.placeholder_subtitle_tv)
        actionBtn = view.findViewById(R.id.placeholder_first_btn)
    }

    private fun initViews(view: View) {
        imageIv.setImageResource(imageRes)
        titleTv.text = view.resources.getString(titleRes)
        subtitleTv.text = view.resources.getString(subtitleRes)
        actionBtn.text = view.resources.getString(btnRes)
        actionBtn.setOnClickListener { onBtnClickedListener.invoke() }
    }
}