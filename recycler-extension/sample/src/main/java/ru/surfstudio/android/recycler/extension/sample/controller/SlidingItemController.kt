package ru.surfstudio.android.recycler.extension.sample.controller

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_sliding_sample.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.view.DeleteSlidingButtonView
import ru.surfstudio.android.recycler.extension.sample.view.FavoritesSlidingButtonView
import ru.surfstudio.android.recycler.extension.sample.view.ShareSlidingButtonView
import ru.surfstudio.android.recycler.extension.slide.BindableSlidingViewHolder

internal class SlidingItemController(
        private val onShareClicked: () -> Unit,
        private val onFavoritesClicked: () -> Unit,
        private val onDeleteClicked: () -> Unit
) : BindableItemController<String, SlidingItemController.Holder>() {

    override fun getItemId(data: String) = data

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
            BindableSlidingViewHolder<String>(parent, R.layout.item_list_sliding_sample) {

        private val titleTv = itemView.sliding_item_title_tv

        override val leftButtons = listOf(ShareSlidingButtonView(context))
        override val rightButtons = listOf(
                FavoritesSlidingButtonView(context),
                DeleteSlidingButtonView(context)
        )

        override fun onButtonClicked(buttonView: View) {
            super.onButtonClicked(buttonView)
            when (buttonView) {
                is FavoritesSlidingButtonView -> {
                    // demo purpose only, don't change state of view this way!
                    buttonView.isInFavorites = !buttonView.isInFavorites
                    // better call callback below and update list from presenter.
                    onFavoritesClicked()
                }
                is ShareSlidingButtonView -> onShareClicked()
                is DeleteSlidingButtonView -> onDeleteClicked()
            }
        }

        override fun onBindButton(data: String, buttonView: View) {
            when (buttonView) {
                is FavoritesSlidingButtonView -> buttonView.isInFavorites = true
            }
        }

        override fun bind(data: String) {
            super.bind(data)
            titleTv.text = data
        }
    }
}