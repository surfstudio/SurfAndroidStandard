package ru.surfstudio.standard.f_debug.scalpel

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.scalpel_widget_layout.view.*
import ru.surfstudio.android.template.f_debug.R
import java.util.concurrent.TimeUnit

/**
 * Виджет, сожержащий [ScalpelFrameLayout] и контролы для его настройки
 */
class ScalpelWidget(context: Context) : RelativeLayout(context) {

    private val scalpelSettings = ScalpelSettings(context)
    private val scalpelManager = ScalpelManager

    private val disposable = CompositeDisposable()

    init {
        inflate()
        initSettingsControls()
        initScalpel()
        initPanel()
    }

    private fun initScalpel() {
        scalpel.setDrawIds(scalpelSettings.drawIds)
        scalpel.setDrawViewClasses(scalpelSettings.drawClasses)
        scalpel.setDrawViews(scalpelSettings.drawViewsContent)
        scalpel.isLayerInteractionEnabled = true
    }

    private fun initSettingsControls() {
        //Switchers
        draw_id_scalpel_settings.setChecked(scalpelSettings.drawIds)
        draw_class_scalpel_settings.setChecked(scalpelSettings.drawClasses)
        draw_views_scalpel_settings.setChecked(scalpelSettings.drawViewsContent)

        draw_id_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawIds = enabled
            scalpel.setDrawIds(enabled)
        }
        draw_class_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawClasses = enabled
            scalpel.setDrawViewClasses(enabled)
        }
        draw_views_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawViewsContent = enabled
            scalpel.setDrawViews(enabled)
        }

        //Range Bar
        views_layers_scalpel_settings.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            scalpel.currentStartViewLayer = leftPinIndex
            scalpel.currentEndViewLayer = rightPinIndex
        }

        disposable.add(Observable.interval(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    scalpel?.let { scalpel ->
                        with(views_layers_scalpel_settings) {
                            val lastTickEnd = tickEnd.toInt()
                            if (lastTickEnd != scalpel.endViewLayer) {
                                tickEnd = scalpel.endViewLayer.toFloat()
                                if (rightIndex == lastTickEnd) {
                                    setRangePinsByIndices(scalpel.currentStartViewLayer, scalpel.endViewLayer)
                                }
                            }
                        }
                    }

                })

        //Hide Btn
        hide_scalpel_settings_btn.setOnClickListener {
            toggleSettingsVisibility()
        }
    }

    private fun initPanel() {
        scalpel_settings_btn.setOnClickListener {
            toggleSettingsVisibility()
        }
        close_scalpel_btn.setOnClickListener {
            scalpelManager.disableScalpel()
        }
    }

    private fun toggleSettingsVisibility() {
        with(scalpel_settings_container) {
            isVisible = isVisible.not()
        }
    }

    private fun inflate() {
        View.inflate(context, R.layout.scalpel_widget_layout, this)
    }

    fun addContentViews(childViews: List<View>) {
        childViews.forEach { scalpel.addView(it) }
    }

    fun extractContentViews(): List<View> {
        val childViews = (0 until scalpel.childCount)
                .map { scalpel.getChildAt(it) }
                .toList()
        scalpel.removeAllViews()
        return childViews
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.clear()
    }
}