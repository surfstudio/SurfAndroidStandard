package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.controller

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.list_item_kitties_divider.view.*
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.data.DividerType
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.data.DividerUi
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class DividerItemController(
        private val onRefreshClickedAction: (DividerType) -> Unit,
        private val onAllClickedAction: (DividerType) -> Unit
) : BindableItemController<DividerUi, DividerItemController.Holder>() {

    override fun getItemId(data: DividerUi): String = "DividerItemController ${data.title}"

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) :
            BindableViewHolder<DividerUi>(parent, R.layout.list_item_kitties_divider) {

        private val titleTv = itemView.divider_title_tv
        private val refreshBtn = itemView.divider_refresh_btn
        private val progressBar = itemView.divider_progress_bar
        private val allBtn = itemView.divider_all_btn

        private lateinit var payload: DividerUi

        init {
            refreshBtn.setOnClickListener { onRefreshClickedAction(payload.type) }
            allBtn.setOnClickListener { onAllClickedAction(payload.type) }
        }

        override fun bind(data: DividerUi) {
            payload = data
            render()
        }

        private fun render() {
            titleTv.text = payload.title
            refreshBtn.isInvisible = payload.isLoading
            progressBar.isInvisible = !payload.isLoading
            allBtn.isGone = !payload.isAllVisible
        }
    }
}