package ru.surfstudio.android.recycler.decorator.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linear_decor_btn.setOnClickListener {
            startActivity(Intent(this, LinearDecoratorActivity::class.java))
        }

        pager_decor_btn.setOnClickListener {
            startActivity(Intent(this, CarouselDecoratorActivity::class.java))
        }

        easy_adapter_decor_btn.setOnClickListener {
            startActivity(Intent(this, EasyAdapterDecoratorActivity::class.java))
        }
    }
}