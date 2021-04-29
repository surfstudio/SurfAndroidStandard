package ru.surfstudio.standard.f_debug.debug.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.DebugCatalogItemLayoutBinding

/**
 * Виджет для элемента каталога debug-screen
 */
class DebugCatalogItemLayout(
        context: Context,
        attributeSet: AttributeSet
): RelativeLayout(context, attributeSet) {

    private val binding = DebugCatalogItemLayoutBinding.inflate(LayoutInflater.from(context))

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
        with(binding){
            debugItemNameTv.text = debugItemName
            debugItemDescriptionTv.text = debugItemDescription
            debugItemDescriptionTv.isVisible = debugItemDescription.isNotEmpty()
            if (debugItemIcon != null){
                debugItemNameTv.setCompoundDrawablesWithIntrinsicBounds(debugItemIcon, null, null, null)
            }
        }

    }
}
