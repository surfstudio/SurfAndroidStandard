package ru.surfstudio.standard.f_debug.scalpel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ScalpelWidgetLayoutBinding
import java.util.concurrent.TimeUnit

/**
 * Виджет, сожержащий [DebugScalpelFrameLayout] и контролы для его настройки
 */
class DebugScalpelWidget(context: Context) : RelativeLayout(context) {

    private val binding = ScalpelWidgetLayoutBinding.inflate(LayoutInflater.from(context))

    private val scalpelSettings = DebugScalpelSettings(context)
    private val scalpelManager = DebugScalpelManager

    private val disposable = CompositeDisposable()

    init {
        initSettingsControls()
        initScalpel()
        initPanel()
    }

    private fun initScalpel() {
        with(binding.debugScalpel) {
            setDrawIds(scalpelSettings.drawIds)
            setDrawViewClasses(scalpelSettings.drawClasses)
            setDrawViews(scalpelSettings.drawViewsContent)
            isLayerInteractionEnabled = true
        }
    }

    private fun initSettingsControls() {
        with(binding) {
            //Switchers
            debugDrawIdScalpelSettings.setChecked(scalpelSettings.drawIds)
            debugDrawClassScalpelSettings.setChecked(scalpelSettings.drawClasses)
            debugDrawViewsScalpelSettings.setChecked(scalpelSettings.drawViewsContent)

            debugDrawIdScalpelSettings.setOnCheckedChangeListener { _, enabled ->
                scalpelSettings.drawIds = enabled
                debugScalpel.setDrawIds(enabled)
            }
            debugDrawClassScalpelSettings.setOnCheckedChangeListener { _, enabled ->
                scalpelSettings.drawClasses = enabled
                debugScalpel.setDrawViewClasses(enabled)
            }
            debugDrawViewsScalpelSettings.setOnCheckedChangeListener { _, enabled ->
                scalpelSettings.drawViewsContent = enabled
                debugScalpel.setDrawViews(enabled)
            }

            //Range Bar
            debugViewsLayersScalpelSettings.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
                debugScalpel.currentStartViewLayer = leftPinIndex
                debugScalpel.currentEndViewLayer = rightPinIndex
            }

            disposable.add(Observable.interval(2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        debugScalpel.let { scalpel ->
                            with(debugViewsLayersScalpelSettings) {
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
            debugHideScalpelSettingsBtn.setOnClickListener {
                toggleSettingsVisibility()
            }
        }

    }

    private fun initPanel() {
        binding.debugScalpelSettingsBtn.setOnClickListener {
            toggleSettingsVisibility()
        }
        binding.debugCloseScalpelBtn.setOnClickListener {
            scalpelManager.disableScalpel()
        }
    }

    private fun toggleSettingsVisibility() {
        binding.debugScalpelSettingsContainer.isVisible = isVisible.not()
    }

    fun addContentViews(childViews: List<View>) {
        childViews.forEach { binding.debugScalpel.addView(it) }
    }

    fun extractContentViews(): List<View> {
        val childViews = (0 until binding.debugScalpel.childCount)
                .map { binding.debugScalpel.getChildAt(it) }
                .toList()
        binding.debugScalpel.removeAllViews()
        return childViews
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.clear()
    }
}