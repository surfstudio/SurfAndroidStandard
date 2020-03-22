package ru.surfstudio.android.recycler.decorator.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatActivityView
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.EasyAdapterDecoratorActivityView
import ru.surfstudio.android.recycler.decorator.sample.list.LinearDecoratorActivityView
import ru.surfstudio.android.recycler.decorator.sample.pager.CarouselDecoratorActivityView

class MainActivityView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linear_decor_btn.setOnClickListener {
            startActivity(Intent(this, LinearDecoratorActivityView::class.java))
        }

        pager_decor_btn.setOnClickListener {
            startActivity(Intent(this, CarouselDecoratorActivityView::class.java))
        }

        easy_adapter_decor_btn.setOnClickListener {
            startActivity(Intent(this, EasyAdapterDecoratorActivityView::class.java))
        }

        chat_btn.setOnClickListener {
            startActivity(Intent(this, ChatActivityView::class.java))
        }
    }
}