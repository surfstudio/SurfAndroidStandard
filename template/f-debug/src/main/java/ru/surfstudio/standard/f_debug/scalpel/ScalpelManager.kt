package ru.surfstudio.standard.f_debug.scalpel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.hardware.SensorManager
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.logger.Logger
import java.util.concurrent.TimeUnit

/**
 * Управляет переходом и закритием режима Scalpel
 */
@SuppressLint("StaticFieldLeak")
object ScalpelManager {
    val shakeDetectedSubject = PublishSubject.create<Long>()
    var currentActivity : Activity? = null

    lateinit var shakeDetector : ShakeDetector

    fun init(app: Application) {
        initShakeDetector()
        listenActivityLifecycle(app)
        listenShake()
    }

    private fun initShakeDetector() {
        shakeDetector = ShakeDetector {
            shakeDetectedSubject.onNext(System.currentTimeMillis())
        }
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_MEDIUM)
    }

    @SuppressLint("CheckResult")
    private fun listenShake() {
        shakeDetectedSubject.buffer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.size >= 1) {
                        toggleScalpel()
                    }
                }
    }

    private fun toggleScalpel() {
        currentActivity?.let {
            if (isScalpelActive()) {
                disableScalpel()
            } else {
                enableScalpel()
            }
        }

    }

    private fun enableScalpel() {
        val scalpelWidget = ScalpelWidget(currentActivity!!)
        val content = currentActivity!!.findViewById<ViewGroup>(android.R.id.content)
        val childViews = (0 until content.childCount)
                .map { content.getChildAt(it) }
                .toList()
        content.removeAllViews()
        content.addView(scalpelWidget,
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT))
        scalpelWidget.addContentViews(childViews)
    }

    fun disableScalpel() {
        currentActivity?.let { activity ->
            val content = activity.findViewById<ViewGroup>(android.R.id.content)
            val scalpelWidget = (0 until content.childCount)
                    .map { content.getChildAt(it)}
                    .filter { it is ScalpelWidget }
                    .last() as ScalpelWidget
            val childViews = scalpelWidget.extractContentViews()
            content.removeAllViews()
            childViews.forEach { content.addView(it) }
        }
    }

    private fun isScalpelActive(): Boolean {
        val content = currentActivity!!.findViewById<ViewGroup>(android.R.id.content)
        return (0..content.childCount - 1)
                .map { content.getChildAt(it) is ScalpelWidget }
                .reduce { prev, new -> prev || new }

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