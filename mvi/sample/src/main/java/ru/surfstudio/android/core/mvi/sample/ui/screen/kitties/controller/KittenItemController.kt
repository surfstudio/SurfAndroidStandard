package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_kitties_kitten.view.*
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitten
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.KittenUi
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.imageloader.ImageLoader

internal class KittenItemController(
        private val onClickedAction: (Kitten) -> Unit
) : BindableItemController<KittenUi, KittenItemController.Holder>() {

    override fun getItemId(data: KittenUi): String = data.name

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<KittenUi>(parent, R.layout.list_item_kitties_kitten) {

        private val avatarIv = itemView.kitten_avatar_iv
        private val nameTv = itemView.kitten_name_tv
        private val clickableView = itemView.kitten_clickable

        private lateinit var payload: KittenUi

        init {
            clickableView.setOnClickListener { onClickedAction(payload.data) }
        }

        override fun bind(data: KittenUi) {
            payload = data
            render()
        }

        private fun render() {
            nameTv.text = payload.name
            ImageLoader.with(itemView.context)
                    .url(payload.picturePreviewUrl)
                    .circle()
                    .into(avatarIv)
        }
    }
}