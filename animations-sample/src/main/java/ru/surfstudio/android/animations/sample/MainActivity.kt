package ru.surfstudio.android.animations.sample

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.scalpel.ScalpelFrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.animations.anim.*
import ru.surfstudio.android.animations.behaviors.BottomButtonBehavior

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
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
        }, 1000)



        val params = bottom_btn.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = BottomButtonBehavior()

        bottom_btn.setOnClickListener {
            Snackbar.make(container, R.string.snackbar_message, Snackbar.LENGTH_SHORT).show()
        }

        //Cross-fade animation
        show_cross_fade_animation_btn.setOnClickListener {
            AnimationUtil.crossfadeViews(first_iv, second_iv)
        }
        reset_cross_fade_animation_btn.setOnClickListener {
            AnimationUtil.crossfadeViews(second_iv, first_iv)
        }

        //Fade-In & Fade-Out
        fade_animation_widget.setShowAnimationCallback { it.fadeOut() }
        fade_animation_widget.setResetAnimationCallback { it.fadeIn() }

        // Pulse animation
        pulse_animation_widget.setResetBtnEnabled(false)
        pulse_animation_widget.setShowAnimationCallback{
            pulse_animation_widget.setShowBtnEnabled(false)
            it.pulseAnimation()
        }

        // New size animation
        new_size_animation_widget.setShowAnimationCallback { it.toSize(200, 200) }
        new_size_animation_widget.setResetAnimationCallback { it.toSize(100, 100) }

        // Slide animation
        slide_animation_widget.setShowAnimationCallback { it.slideOut(Gravity.END) }
        slide_animation_widget.setResetAnimationCallback { it.slideIn(Gravity.START) }
    }
}
