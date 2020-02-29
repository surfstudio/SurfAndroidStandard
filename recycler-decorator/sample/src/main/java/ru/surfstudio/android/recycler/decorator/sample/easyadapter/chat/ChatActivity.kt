package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat

import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Builder
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.*
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.ChatDecor
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor.ChatDecorOffset

class ChatActivity : AppCompatActivity() {

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
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = easyAdapter
        }

        val decorator = Builder()
                .underlay(ChatDecor(this))
                .offset(chatController.viewType() to ChatDecorOffset())
                .build()

        recycler_view.addItemDecoration(decorator)

        val items = ItemList.create()

        repeat(150) { number ->
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