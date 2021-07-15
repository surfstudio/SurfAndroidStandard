package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher

import ru.surfstudio.android.common.tools.sample.R
import ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.data.Item
import ru.surfstudio.android.core.mvp.model.ScreenModel

internal data class StatusBarSwitcherScreenModel(
        var isAutoSwitchingEnabled: Boolean = true,
        var statusBarHeight: Int = 0,
        val sampleList: List<Item> = listOf(
                Item.Image("https://placekitten.com/408/287"),
                Item.Image("https://placekitten.com/200/287"),
                Item.Image("https://placekitten.com/200/286"),
                Item.Image("https://placekitten.com/96/140"),
                Item.Color(R.color.statusBarSwitcherSampleColor1),
                Item.Space(),
                Item.Color(R.color.statusBarSwitcherSampleColor2),
                Item.Space(),
                Item.Color(R.color.statusBarSwitcherSampleColor3),
                Item.Space()
        )
) : ScreenModel()