package ru.tricolor.android.application.logger.user_actions_logger.base

import com.google.gson.annotations.SerializedName
import ru.tricolor.android.application.logger.user_actions_logger.SWIPE_GESTURE

open class BasicSwipe(
    @SerializedName("direction")
    private val direction: Direction? = null
) : BasicEvent(SWIPE_GESTURE) {

    // направление свайпа
    enum class Direction { LEFT, RIGHT, UP, DOWN }
}