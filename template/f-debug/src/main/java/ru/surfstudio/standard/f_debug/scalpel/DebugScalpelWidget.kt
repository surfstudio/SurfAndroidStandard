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
 * Виджет, сожержащий [DebugScalpelFrameLayout] и контролы для его настройки
 */
class DebugScalpelWidget(context: Context) : RelativeLayout(context) {

    private val scalpelSettings = DebugScalpelSettings(context)
    private val scalpelManager = DebugScalpelManager

    private val disposable = CompositeDisposable()

    init {
        inflate()
        initSettingsControls()
        initScalpel()
        initPanel()
    }

    private fun initScalpel() {
        debug_scalpel.setDrawIds(scalpelSettings.drawIds)
        debug_scalpel.setDrawViewClasses(scalpelSettings.drawClasses)
        debug_scalpel.setDrawViews(scalpelSettings.drawViewsContent)
        debug_scalpel.isLayerInteractionEnabled = true
    }

    private fun initSettingsControls() {
        //Switchers
        debug_draw_id_scalpel_settings.setChecked(scalpelSettings.drawIds)
        debug_draw_class_scalpel_settings.setChecked(scalpelSettings.drawClasses)
        debug_draw_views_scalpel_settings.setChecked(scalpelSettings.drawViewsContent)

        debug_draw_id_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawIds = enabled
            debug_scalpel.setDrawIds(enabled)
        }
        debug_draw_class_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawClasses = enabled
            debug_scalpel.setDrawViewClasses(enabled)
        }
        debug_draw_views_scalpel_settings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawViewsContent = enabled
            debug_scalpel.setDrawViews(enabled)
        }

        //Range Bar
        debug_views_layers_scalpel_settings.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            debug_scalpel.currentStartViewLayer = leftPinIndex
            debug_scalpel.currentEndViewLayer = rightPinIndex
        }

        disposable.add(Observable.interval(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    debug_scalpel?.let { scalpel ->
                        with(debug_views_layers_scalpel_settings) {
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
        debug_hide_scalpel_settings_btn.setOnClickListener {
            toggleSettingsVisibility()
        }
    }

    private fun initPanel() {
        debug_scalpel_settings_btn.setOnClickListener {
            toggleSettingsVisibility()
        }
        debug_close_scalpel_btn.setOnClickListener {
            scalpelManager.disableScalpel()
        }
    }

    private fun toggleSettingsVisibility() {
        with(debug_scalpel_settings_container) {
            isVisible = isVisible.not()
        }
    }

    private fun inflate() {
        View.inflate(context, R.layout.scalpel_widget_layout, this)
    }

    fun addContentViews(childViews: List<View>) {
        childViews.forEach { debug_scalpel.addView(it) }
    }

    fun extractContentViews(): List<View> {
        val childViews = (0 until debug_scalpel.childCount)
                .map { debug_scalpel.getChildAt(it) }
                .toList()
        debug_scalpel.removeAllViews()
        return childViews
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.clear()
    }
}