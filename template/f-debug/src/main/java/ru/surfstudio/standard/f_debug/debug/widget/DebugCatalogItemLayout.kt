package ru.surfstudio.standard.f_debug.debug.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.debug_catalog_item_layout.view.*
import ru.surfstudio.android.template.f_debug.R

/**
 * Виджет для элемента каталога debug-screen
 */
class DebugCatalogItemLayout(
        context: Context,
        attributeSet: AttributeSet
): RelativeLayout(context, attributeSet) {

    private lateinit var debugItemName: String

    init {
        View.inflate(context, R.layout.debug_catalog_item_layout, this)
        obtainAttributes(context, attributeSet)
        updateView()
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DebugCatalogItemLayout)
        debugItemName = typedArray.getString(R.styleable.DebugCatalogItemLayout_catalog_item_name) ?: ""
        typedArray.recycle()
    }

    private fun updateView() {
        debug_item_tv.text = debugItemName
    }
}