package ru.surfstudio.android.recycler.decorator.sample.pager

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.Controller
import ru.surfstudio.android.recycler.decorator.sample.list.decor.RoundViewHoldersGroupDrawer
import ru.surfstudio.android.recycler.decorator.sample.list.decor.SimpleOffsetDrawer
import kotlinx.android.synthetic.main.activity_pager.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.LinePagerIndicatorDecoration
import ru.surfstudio.android.recycler.decorator.sample.toPx

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CarouselDecoratorActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()

    private val controller = Controller(R.layout.item_controller_pager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        init()
    }


    private fun init() {
        pager_rv.apply {
            layoutManager = LinearLayoutManager(this@CarouselDecoratorActivity, RecyclerView.HORIZONTAL, false)
            adapter = easyAdapter.apply { isFirstInvisibleItemEnabled = false }
            PagerSnapHelper().attachToRecyclerView(pager_rv)
        }

        val roundViewHoldersGroupDrawer = RoundViewHoldersGroupDrawer(8.toPx.toFloat())

        val simpleOffsetDrawer2 = SimpleOffsetDrawer(
            left = 16.toPx,
            right = 16.toPx
        )

        val decorator2 = Decorator.Builder()
            .underlay(roundViewHoldersGroupDrawer)
            .overlay(LinePagerIndicatorDecoration())
            .offset(simpleOffsetDrawer2)
            .build()

        pager_rv.addItemDecoration(decorator2)

        val itemList = ItemList.create()

        (0..5).forEach { _ ->
            itemList.add(controller)
        }

        easyAdapter.setItems(itemList)
    }
}
