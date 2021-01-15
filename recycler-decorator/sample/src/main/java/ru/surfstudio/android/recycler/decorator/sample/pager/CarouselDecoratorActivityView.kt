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
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.GradientBackgroundDecoration
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.SlideLinePagerIndicatorDecoration
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.ScaleLinePageIndicatorDecoration
import ru.surfstudio.android.recycler.decorator.sample.toPx

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CarouselDecoratorActivityView : AppCompatActivity() {

    private val lineEasyAdapter = EasyAdapter()

    private val controller = Controller(R.layout.item_controller_pager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        init()
    }


    private fun init() {
        pager_line_rv.apply {
            layoutManager = LinearLayoutManager(this@CarouselDecoratorActivityView, RecyclerView.HORIZONTAL, false)
            adapter = lineEasyAdapter.apply { isFirstInvisibleItemEnabled = false }
            PagerSnapHelper().attachToRecyclerView(pager_line_rv)
        }

        pager_scale_line_rv.apply {
            layoutManager = LinearLayoutManager(this@CarouselDecoratorActivityView, RecyclerView.HORIZONTAL, false)
            adapter = lineEasyAdapter.apply { isFirstInvisibleItemEnabled = false }
            PagerSnapHelper().attachToRecyclerView(pager_scale_line_rv)
        }

        val roundViewHoldersGroupDrawer = RoundViewHoldersGroupDrawer(8.toPx.toFloat())

        val simpleOffsetDrawer2 = SimpleOffsetDrawer(
                left = 16.toPx,
                right = 16.toPx
        )

        val decoratorLine = Decorator.Builder()
                .underlay(roundViewHoldersGroupDrawer)
                .overlay(SlideLinePagerIndicatorDecoration())
                .offset(simpleOffsetDrawer2)
                .build()

        pager_line_rv.addItemDecoration(decoratorLine)

        val decoratorScaleLine = Decorator.Builder()
                .overlay(GradientBackgroundDecoration())
                .overlay(ScaleLinePageIndicatorDecoration())
                .offset(simpleOffsetDrawer2)
                .build()
        pager_scale_line_rv.addItemDecoration(decoratorScaleLine)
        lineEasyAdapter.setInfiniteScroll(false)

        val itemList = ItemList.create()

        (0..5).forEach { _ ->
            itemList.add(controller)
        }

        lineEasyAdapter.setItems(itemList)
    }
}
