package ru.surfstudio.android.animations.sample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.animation_layout.view.*
import ru.surfstudio.android.animations.sample.R

class AnimationWidget(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    lateinit var firstImageView: ImageView
    lateinit var secondImageView: ImageView

    init {
        initViews()
        initAttr(context, attrs)
    }

    private fun initViews() {
        View.inflate(context, R.layout.animation_layout, this)
        firstImageView = findViewById(R.id.first_iv)
        secondImageView = findViewById(R.id.second_iv)
    }

    private fun initAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AnimationWidget)
        try {
            animation_name_tv.text = a.getString(R.styleable.AnimationWidget_animationName)
        } finally {
            a.recycle()
        }
    }
}