@file:Suppress("unused")

package ru.tricolor.android.application.logger.user_actions_logger.aspects

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import ru.tricolor.android.application.logger.user_actions_logger.ViewSwipe
import ru.tricolor.android.application.logger.user_actions_logger.base.BasicSwipe
import ru.tricolor.android.ui.tools.extension.getSnapPosition

/**
 * Аспект, отвечающий за логгирование свайпов snap'ов
 */
@Aspect
class SnapLoggingAspect : BaseAspect() {

    private lateinit var snapHelper: SnapHelper
    private var snapPosition: Int = RecyclerView.NO_POSITION

    /**
     *  Обработчик вызова конструктора класса SnapOnScrollListener
     *
     *  @param snapHelper - используемый в классе SnapHelper
     */
    @Before("execution(ru.tricolor.android.ui.recylcer.SnapOnScrollListener.new(..)) && args(snapHelper, ..)")
    fun onSnapOnScrollListenerCreationAdvice(snapHelper: SnapHelper) {
        this.snapHelper = snapHelper
    }

    /**
     *  Обработчик обновления позиции snap'a
     *
     *  @param newSnapPosition - позиция snap'a на переднем плане
     */
    @After("set(private int ru.tricolor.android.ui.recylcer.SnapOnScrollListener.snapPosition) && args(newSnapPosition)")
    fun afterSnapPositionFieldAccess(newSnapPosition: Int) {
        snapPosition = newSnapPosition
    }

    /**
     *  Обработчик свайпов snap'ов
     *
     *  @param recyclerView - контейнер для snap'ов
     */
    @Before("call(void ru.tricolor.android.ui.recylcer.SnapOnScrollListener.maybeNotifySnapPositionChange(..)) && args(recyclerView)")
    fun onSnapOnScrollListenerSwipeAdvice(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = snapPosition - this.snapPosition

        var swipeDirection: BasicSwipe.Direction? = null

        if (snapPositionChanged > 0) {
            swipeDirection = BasicSwipe.Direction.RIGHT
        } else if (snapPositionChanged < 0) {
            swipeDirection = BasicSwipe.Direction.LEFT
        }

        if (swipeDirection != null && this.snapPosition != RecyclerView.NO_POSITION) {
            log(ViewSwipe(recyclerView, swipeDirection).toString())
        }
    }
}