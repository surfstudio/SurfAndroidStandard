package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers

import android.view.ViewGroup
import kotlinx.android.synthetic.main.stub_controller.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.loadstate.sample.R


/**
 * Контроллер для пустых состний в списках
 * */
class StubLoadStateController : BindableItemController<StubData, StubLoadStateController.Holder>() {

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    override fun getItemId(data: StubData) = "${data.id}${data.isAnimated}"

    inner class Holder(parent: ViewGroup) : BindableViewHolder<StubData>(parent, R.layout.stub_controller) {

        val shimmer = itemView.stub_controller_shimmer

        override fun bind(data: StubData) {
            animateShimmer(data.isAnimated)
        }

        private fun animateShimmer(isAnimated: Boolean) {
            shimmer.post {
                if (isAnimated) {
                    shimmer.startShimmer()
                } else {
                    shimmer.stopShimmer()
                }
            }
        }
    }
}

/**
 * Вспомогательный класс данных, отражающий номер и состояние заглушки
 */
class StubData(
        val id: Int,
        val isAnimated: Boolean)