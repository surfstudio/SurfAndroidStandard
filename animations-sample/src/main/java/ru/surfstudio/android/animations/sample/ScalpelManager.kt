package ru.surfstudio.android.animations.sample

import android.app.Activity
import android.app.Application
import android.hardware.SensorManager
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import java.util.concurrent.TimeUnit

class ScalpelManager {
    val shakeDetectedSubject = PublishSubject.create<Long>()
    var currentActivity : Activity? = null

    lateinit var shakeDetector : ShakeDetector

    fun init(app: Application){

        initShakeDetector()
        listenActivityLifecycle(app)
        listenShake()
    }

    private fun initShakeDetector() {
        shakeDetector = ShakeDetector {
            shakeDetectedSubject.onNext(System.currentTimeMillis())
            val scalpel = ScalpelFrameLayout(this)
            val content = this.findViewById<ViewGroup>(android.R.id.content)
            val childViews = (0..content.childCount - 1)
                    .map { content.getChildAt(it) }
                    .toList()
            Log.d("AAA", childViews.toString())
            content.removeAllViews()
            content.addView(scalpel,
                    FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT))
            childViews.forEach { scalpel.addView(it) }
            scalpel.isLayerInteractionEnabled = true
            //scalpel.setDrawIds(true)
            ru.surfstudio.android.logger.Logger.d("AAA", "Shake")
        }
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_MEDIUM)
    }

    private fun listenShake() {
        shakeDetectedSubject.buffer(1000, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (it.size >= 3) {
                        toggleScalpel()
                    }
                }
    }

    private fun toggleScalpel() {


    }

    private fun listenActivityLifecycle(app: Application) {
        app.registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
                shakeDetector.start(app.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager)
            }

            override fun onActivityPaused(activity: Activity) {
                currentActivity = null
                shakeDetector.stop()

            }
        })
    }
}