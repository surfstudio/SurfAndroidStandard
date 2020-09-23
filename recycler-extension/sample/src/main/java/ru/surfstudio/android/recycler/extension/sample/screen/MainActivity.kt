package ru.surfstudio.android.recycler.extension.sample.screen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.recycler.extension.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListener(show_sticky_recycler_btn, StickyRecyclerActivity::class.java)
        setOnClickListener(show_carousel_btn, CarouselActivity::class.java)
        setOnClickListener(show_sliding_item_btn, SlidingItemsActivity::class.java)
    }

    private fun setOnClickListener(btn: Button, activityClass: Class<*>) {
        btn.setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
