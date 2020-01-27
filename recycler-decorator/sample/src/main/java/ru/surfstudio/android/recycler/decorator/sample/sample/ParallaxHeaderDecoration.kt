package ru.surfstudio.android.recycler.decorator.sample.sample

import android.content.Context
import android.graphics.*
import android.view.View

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

import ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor

class ParallaxHeaderDecoration(context: Context, @DrawableRes resId: Int) : ViewHolderDecor {

    private val mImage: Bitmap = BitmapFactory.decodeResource(context.resources, resId)

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {

        if (recyclerView.getChildAdapterPosition(view) != 2) return

        val offset = view.top / 3
        canvas.drawBitmap(
            mImage,
            Rect(0, offset, mImage.width, view.height + offset),
            Rect(view.left, view.top, view.right, view.bottom),
            null
        )
    }
}
