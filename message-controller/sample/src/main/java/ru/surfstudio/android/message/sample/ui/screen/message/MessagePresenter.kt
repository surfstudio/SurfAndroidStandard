package ru.surfstudio.android.message.sample.ui.screen.message

import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.SnackParams
import ru.surfstudio.android.message.ToastParams
import ru.surfstudio.android.message.sample.R
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class MessagePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        val route: MessageActivityRoute
) : BasePresenter<MessageActivityView>(basePresenterDependency) {

    private val sm: MessageScreenModel = MessageScreenModel()

    private var downTimer: CountDownTimer? = null

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            startTimer()
        } else {
            view.render(sm)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun startTimer(tickStart: Int = 0) {
        initOrUpdateTimer(tickStart)
        downTimer?.start()
    }

    private fun stopTimer() {
        downTimer?.cancel()
    }

    private fun initOrUpdateTimer(tickStart: Int = 0) {
        stopTimer()

        downTimer = object : CountDownTimer(
                TimeUnit.SECONDS.toMillis(60),
                TimeUnit.SECONDS.toMillis(1)
        ) {

            var tickCount = tickStart

            override fun onTick(sec: Long) {
                tickCount++
                val gravities = arrayOf(Gravity.CENTER, Gravity.START, Gravity.END)
                val randomColor = Random().randInt(0, Integer.MAX_VALUE - 1)
                val randomGravity = gravities[Random().randInt(0, 2)]
                val message = "tick #$tickCount"
                if (tickCount % 2 != 0) {
                    sm.snackParams = SnackParams(
                            message = message,
                            backgroundColor = randomColor,
                            action = "action",
                            actionColor = randomColor / 2,
                            duration = Snackbar.LENGTH_INDEFINITE
                    )
                    sm.toastParams = null
                } else {
                    sm.snackParams = null

                    var xOffset = 0
                    var yOffset = 0
                    var customView: View? = null

                    if (tickCount % 4 == 0) {
                        xOffset = tickCount * 2
                        yOffset = xOffset
                        customView = LayoutInflater.from(view).inflate(R.layout.placeholder_view_loading_strategy, null)
                    }
                    sm.toastParams = ToastParams(
                            message = message,
                            xOffset = xOffset,
                            yOffset = yOffset,
                            gravity = randomGravity,
                            customView = customView
                    )
                }
                view.render(sm)
            }

            override fun onFinish() {
                if (!route.infiniteLoop) {
                    stopTimer()
                } else {
                    startTimer(tickCount)
                }
            }
        }
    }

    private fun Random.randInt(min: Int, max: Int): Int {
        return nextInt(max - min + 1) + min
    }
}