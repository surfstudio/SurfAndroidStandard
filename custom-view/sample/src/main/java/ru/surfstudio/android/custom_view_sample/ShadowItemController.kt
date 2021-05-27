package ru.surfstudio.android.custom_view_sample

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_shadow.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.imageloader.ImageLoader

class ShadowItemController : BindableItemController<String, ShadowItemController.Holder>() {

    override fun getItemId(data: String): String = data

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) :
            BindableViewHolder<String>(parent, R.layout.list_item_shadow) {

        private val shadowIv = itemView.shadow_picture_iv
        private val shadowContainer = itemView.shadow_picture_container

        override fun bind(data: String) {
            ImageLoader.with(shadowIv.context)
                    .url(data)
                    .preview(R.drawable.bg_rounded_card)
                    .centerCrop()
                    .into(shadowIv, onCompleteLambda = { drawable, _ ->
                        shadowIv.setImageDrawable(drawable)
                        shadowContainer.redrawShadow()
                    })
        }
    }
}