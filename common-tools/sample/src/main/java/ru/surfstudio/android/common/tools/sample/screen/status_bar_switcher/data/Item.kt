package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.data

import androidx.annotation.ColorRes

internal sealed class Item {

    data class Color(@ColorRes val color: Int) : Item()
    data class Image(val url: String) : Item()
    class Space : Item()
}