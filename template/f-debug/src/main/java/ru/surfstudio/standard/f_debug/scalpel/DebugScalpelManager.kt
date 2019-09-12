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
import java.util.concurrent.TimeUnit

/**
 * Управляет переходом и закритием режима Scalpel
 */
@SuppressLint("StaticFieldLeak")
object DebugScalpelManager {
    private val shakeDetectedSubject = PublishSubject.create<Long>()
    private var currentActivity: Activity? = null

    private lateinit var shakeDetector: DebugShakeDetector

    fun init(app: Application) {
        initShakeDetector()
        listenActivityLifecycle(app)
        listenShake()
    }

    private fun initShakeDetector() {
        shakeDetector = DebugShakeDetector {
            shakeDetectedSubject.onNext(System.currentTimeMillis())
        }
        shakeDetector.setSensitivity(DebugShakeDetector.SENSITIVITY_MEDIUM)
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

    private fun listenActivityLifecycle(app: Application) {
        app.registerActivityLifecycleCallbacks(object : DebugDefaultActivityLifecycleCallbacks() {
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
        val scalpelWidget = DebugScalpelWidget(currentActivity!!)
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
                    .filter { it is DebugScalpelWidget }
                    .last() as DebugScalpelWidget
            val childViews = scalpelWidget.extractContentViews()
            content.removeAllViews()
            childViews.forEach { content.addView(it) }
        }
    }

    private fun isScalpelActive(): Boolean {
        val content = currentActivity!!.findViewById<ViewGroup>(android.R.id.content)
        return (0 until content.childCount)
                .map { content.getChildAt(it) is DebugScalpelWidget }
                .reduce { prev, new -> prev || new }

    }
}