package ru.surfstudio.standard.f_debug.debug.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
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
    private lateinit var debugItemDescription: String
    private var debugItemIcon: Drawable? = null

    init {
        View.inflate(context, R.layout.debug_catalog_item_layout, this)
        obtainAttributes(context, attributeSet)
        updateView()
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DebugCatalogItemLayout)
        debugItemName = typedArray.getString(R.styleable.DebugCatalogItemLayout_debug_catalog_item_name) ?: ""
        debugItemDescription = typedArray.getString(R.styleable.DebugCatalogItemLayout_debug_catalog_item_description) ?: ""
        debugItemIcon = typedArray.getDrawable(R.styleable.DebugCatalogItemLayout_debug_catalog_item_icon)
        typedArray.recycle()
    }

    private fun updateView() {
        debug_item_name_tv.text = debugItemName
        debug_item_description_tv.text = debugItemDescription
        debug_item_description_tv.isVisible = debugItemDescription.isNotEmpty()
        if (debugItemIcon != null){
            debug_item_name_tv.setCompoundDrawablesWithIntrinsicBounds(debugItemIcon, null, null, null)
        }
    }
}