package ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.controller.BindableController
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.decor.BindableDecor
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.decor.BindableOffset
import ru.surfstudio.android.recycler.decorator.sample.list.decor.Gap
import ru.surfstudio.android.recycler.decorator.sample.list.decor.Rules
import ru.surfstudio.android.recycler.decorator.sample.toPx

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class EasyAdapterDecoratorActivityView : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()

    private val bindableController = BindableController(R.layout.item_controller_short_card)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        init()
    }

    private fun init() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EasyAdapterDecoratorActivityView)
            adapter = easyAdapter
        }

        val easyDecor = BindableDecor(Gap(
                resources.getColor(R.color.gray_A150),
                2.toPx,
                paddingStart = 16.toPx,
                paddingEnd = 16.toPx,
                rule = Rules.MIDDLE
        ))

        val bindableOffset = BindableOffset()

        val decorator2 = Decorator.Builder()
                .overlay(easyDecor)
                .offset(bindableOffset)
                .build()

        recycler_view.addItemDecoration(decorator2)

        val items = ItemList.create()

        (0..2).forEach { number ->
            items.add(1, bindableController)
        }

        (0..2).forEach { number ->
            items.add(2, bindableController)
        }

        (0..2).forEach { number ->
            items.add(3, bindableController)
        }

        easyAdapter.setItems(items)
    }
}