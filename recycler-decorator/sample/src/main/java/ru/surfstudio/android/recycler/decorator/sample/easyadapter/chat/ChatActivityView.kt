package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat

import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.ChatMessageController
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.MessageTimeController
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.ChatDecorOffset
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.ChatMessageDecor
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.MessageTimeDecor
import ru.surfstudio.android.recycler.decorator.easyadapter.offset
import ru.surfstudio.android.recycler.decorator.easyadapter.underlay

class ChatActivityView : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()
            .apply {
                isFirstInvisibleItemEnabled = true
            }

    private val chatController = ChatMessageController()
    private val messageTimeController = MessageTimeController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        init()
    }

    private fun init() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ChatActivityView)
            adapter = easyAdapter
        }

        val decorator = Decorator.Builder()
                .underlay(messageTimeController.viewType() to MessageTimeDecor(this))
                .underlay(chatController.viewType() to ChatMessageDecor(this))
                .offset(chatController.viewType() to ChatDecorOffset())
                .build()

        recycler_view.addItemDecoration(decorator)

        val items = ItemList.create()

        repeat(1500) { number ->
            if(number.rem(4) == 0) {
                val date = DateFormat.format("dd MMMM yyyy HH:mm:ss", System.currentTimeMillis())
                items.add(date.toString(), messageTimeController)
            }
            val direction = ChatMessageDirection.values()[(0..1).random()]
            items.add(ChatObject(number, direction), chatController)
        }

        easyAdapter.setItems(items)
    }
}