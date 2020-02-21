package ru.surfstudio.android.recycler.decorator.sample

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                setFirstInvisibleItemEnabled(true)
            }

    private val chatController = ChatMessageController()

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

        /*recycler_view.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDrawOver(c, parent, state)


            }

            override fun onDraw(c: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, recyclerView, state)
                recycler_view.children.forEach {  view ->
                    val itemPosition = recyclerView.getChildAdapterPosition(view)
                    Log.d("ChatDecor","Call draw for position: $itemPosition")
                }

            }

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
            }
        })*/

        val items = ItemList.create()

        repeat(150) { number ->
            val direction = ChatMessageDirection.values()[(0..1).random()]
            items.add(ChatObject(number, direction), chatController)
        }

        easyAdapter.setItems(items)
    }
}