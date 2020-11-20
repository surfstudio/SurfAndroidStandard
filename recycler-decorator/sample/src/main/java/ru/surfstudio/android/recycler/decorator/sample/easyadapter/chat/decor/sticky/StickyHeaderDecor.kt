package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.sticky

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator

class StickyHeaderDecor() : Decorator.RecyclerViewDecor {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentHeaderBitmap: Bitmap? = null

    override fun draw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {

        //find all StiсkyHolders
        val stickyHolders = recyclerView.children
            .map { recyclerView.findContainingViewHolder(it) }
            .filter { it is StiсkyHolder }

        //ensure that we see all attached holders
        stickyHolders.forEach { it?.itemView?.alpha = 1f }

        //take first StiсkyHolder and his params
        val viewHolder = stickyHolders.first()

        val viewHolderBitmap = viewHolder?.itemView?.drawToBitmap()
        val viewHolderY = viewHolder?.itemView?.y ?: 0f

        //init headerBitmap if need
        if (currentHeaderBitmap == null || viewHolderY <= 0f) {
            currentHeaderBitmap = viewHolderBitmap
        }

        //calculate bitmap top offset
        val bitmapHeight = currentHeaderBitmap?.height ?: 0
        val bitmapTopOffset = if (0 <= viewHolderY && viewHolderY <= bitmapHeight) {
            viewHolderY - bitmapHeight
        } else {
            0f
        }

        //hide view
        viewHolder?.itemView?.alpha = if (viewHolderY < 0f) {
            0f
        } else {
            1f
        }

        //draw bitmap header
        currentHeaderBitmap?.let {
            canvas.drawBitmap(it, 0f, bitmapTopOffset, paint)
        }
    }
}