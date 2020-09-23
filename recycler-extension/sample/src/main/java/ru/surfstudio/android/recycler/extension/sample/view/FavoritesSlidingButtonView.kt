package ru.surfstudio.android.recycler.extension.sample.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import ru.surfstudio.android.recycler.extension.sample.R

internal class FavoritesSlidingButtonView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : BaseSlidingButtonView(context, attrs, defStyleAttr) {

    var isInFavorites: Boolean = false
        set(value) {
            field = value
            render(value)
        }

    init {
        setBackgroundColor(Color.parseColor("#ff9999"))
        render(isInFavorites)
    }

    private fun render(isInFavorites: Boolean) {
        val iconRes = when (isInFavorites) {
            true -> R.drawable.ic_favorite
            false -> R.drawable.ic_favorite_hollow
        }
        val text = when (isInFavorites) {
            true -> "Remove from favorites"
            false -> "Add to favorites"
        }
        setIconRes(iconRes)
        setText(text)
    }

}