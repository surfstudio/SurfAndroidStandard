package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.item_key_view.view.*
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.ui.view.keyboard.Key
import ru.surfstudio.standard.ui.view.keyboard.KeyView

/**
 * Базовый ViewHolder для создания кнопки для кастомной клавиатуры.
 * todo удалить, если не требуется на проекте
 */
open class BaseKeyHolder<T : Key>(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int = R.layout.item_key_view
) : BindableViewHolder<T>(parent, layoutRes) {

    protected val keyView: KeyView = itemView.key_view

    @CallSuper
    override fun bind(key: T) {
        keyView.key = key
    }
}