package ru.surfstudio.android.recycler.decorator.sample

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Builder
import ru.surfstudio.android.recycler.decorator.sample.controllers.*
import ru.surfstudio.android.recycler.decorator.sample.easydecor.offset
import ru.surfstudio.android.recycler.decorator.sample.easydecor.underlay

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
                .offset(ChatDecorOffset())
                .build()

        recycler_view.addItemDecoration(decorator)

        val items = ItemList.create()

        repeat(150) { number ->
            val direction = ChatMessageDirection.values()[(0..1).random()]

            if(number.rem(4) == 0) {
                val date = DateFormat.format("dd:mm:YYYY HH:mm", System.currentTimeMillis())
                items.add(date.toString(), messageTimeController)
            }
            items.add(ChatObject(number, direction), chatController)
        }

        easyAdapter.setItems(items)
    }
}