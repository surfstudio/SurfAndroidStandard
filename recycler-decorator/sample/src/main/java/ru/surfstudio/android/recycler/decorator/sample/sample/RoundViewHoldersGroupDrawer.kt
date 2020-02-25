package ru.surfstudio.android.recycler.decorator.sample.sample

import android.graphics.Canvas
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.sample.round.RoundMode
import ru.surfstudio.android.recycler.decorator.sample.round.RoundOutlineProvider
import ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class RoundViewHoldersGroupDrawer(
    private val cornerRadius: Float,
    private val roundPolitic: RoundPolitic = Every(RoundMode.ALL))
    : ViewHolderDecor {

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {

        val viewHolder = recyclerView.getChildViewHolder(view)
        val nextViewHolder = recyclerView.findViewHolderForAdapterPosition(viewHolder.adapterPosition + 1)
        val previousChildViewHolder = recyclerView.findViewHolderForAdapterPosition(viewHolder.adapterPosition - 1)

        if (cornerRadius.compareTo(0f) != 0) {
            val roundMode = getRoundMode(previousChildViewHolder, viewHolder, nextViewHolder)
            val outlineProvider = view.outlineProvider
            if (outlineProvider is RoundOutlineProvider) {
                outlineProvider.roundMode = roundMode
                view.invalidateOutline()
            } else {
                view.outlineProvider = RoundOutlineProvider(cornerRadius, roundMode)
                view.clipToOutline = true
            }
        }
    }


    private fun getRoundMode(
        previousChildViewHolder: RecyclerView.ViewHolder?,
        currentViewHolder: RecyclerView.ViewHolder?,
        nextChildViewHolder: RecyclerView.ViewHolder?
    ): RoundMode {

        val previousHolderItemType = previousChildViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER
        val currentHolderItemType = currentViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER
        val nextHolderItemType = nextChildViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER

        if(roundPolitic is Every) return roundPolitic.roundMode

        return when {
            previousHolderItemType != currentHolderItemType && currentHolderItemType != nextHolderItemType -> RoundMode.ALL
            previousHolderItemType != currentHolderItemType && currentHolderItemType == nextHolderItemType -> RoundMode.TOP
            previousHolderItemType == currentHolderItemType && currentHolderItemType != nextHolderItemType -> RoundMode.BOTTOM
            previousHolderItemType == currentHolderItemType && currentHolderItemType == nextHolderItemType -> RoundMode.NONE
            else -> RoundMode.NONE
        }
    }
}

sealed class RoundPolitic
class Every(val roundMode: RoundMode): RoundPolitic()
class Group(): RoundPolitic()