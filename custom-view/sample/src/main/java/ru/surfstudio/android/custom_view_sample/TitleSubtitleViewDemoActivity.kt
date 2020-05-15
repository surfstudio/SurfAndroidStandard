package ru.surfstudio.android.custom_view_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tv.*
import androidx.core.content.ContextCompat.getDrawable

class TitleSubtitleViewDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv)

        fourth_tsv.setTitleDrawables(
                startDrawableId = R.drawable.ic_brightness_7_red_24dp,
                endDrawableId = R.drawable.ic_brightness_7_red_24dp
        )

        fifth_tsv.subTitleStartDrawableId = R.drawable.ic_brightness_7_red_24dp

        sixth_tsv.titleStartDrawable = getDrawable(this, R.drawable.ic_brightness_7_red_24dp)

        seventh_tsv.setSubTitleDrawables(
                topDrawable = getDrawable(this, R.drawable.ic_brightness_7_red_24dp),
                bottomDrawable = getDrawable(this, R.drawable.ic_brightness_7_red_24dp)
        )
    }
}