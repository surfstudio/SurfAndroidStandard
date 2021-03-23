package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.controller

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import ru.surfstudio.android.common.tools.sample.R
import ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.data.Item
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.imageloader.ImageLoader

internal class ImageItemController : BindableItemController<Item, ImageItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(item: Item) = "ImageItemController$item"

    class Holder(parent: ViewGroup) : BindableViewHolder<Item>(parent, R.layout.item_image) {

        private val imageView = itemView as ImageView

        private val context get() = itemView.context.applicationContext

        override fun bind(item: Item) {
            imageView.background = null
            imageView.setImageDrawable(null)
            when (item) {
                is Item.Image -> renderImage(item)
                is Item.Color -> renderColor(item)
                is Item.Space -> renderSpace()
            }
        }

        private fun renderImage(item: Item.Image) {
            updateImageViewHeight(context.resources.getDimensionPixelOffset(R.dimen.imageItemHeight))
            ImageLoader.with(context)
                    .url(item.url)
                    .centerCrop()
                    .into(imageView)
        }

        private fun renderColor(item: Item.Color) {
            updateImageViewHeight(context.resources.getDimensionPixelOffset(R.dimen.colorItemHeight))
            imageView.setBackgroundColor(ContextCompat.getColor(context, item.color))
        }

        private fun renderSpace() {
            updateImageViewHeight(context.resources.getDimensionPixelOffset(R.dimen.spaceItemHeight))
        }

        private fun updateImageViewHeight(newHeight: Int) {
            imageView.updateLayoutParams {
                height = newHeight
            }
        }
    }
}