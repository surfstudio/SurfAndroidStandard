package ru.surfstudio.android.recycler.decorator.sample

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_linear_decoration.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Builder
import ru.surfstudio.android.recycler.decorator.sample.controllers.BindableController
import ru.surfstudio.android.recycler.decorator.sample.easydecor.offset
import ru.surfstudio.android.recycler.decorator.sample.easydecor.overlay
import ru.surfstudio.android.recycler.decorator.sample.sample.Gap
import ru.surfstudio.android.recycler.decorator.sample.sample.Rules

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class EasyAdapterDecoratorActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()

    private val bindableController = BindableController(R.layout.item_controller_short_card)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_decoration)
        init()
    }

    private fun init() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EasyAdapterDecoratorActivity)
            adapter = easyAdapter
        }

        val easyDecor = BindableController.BindableDecor(Gap(
                resources.getColor(R.color.gray_A150),
                2.toPx,
                paddingStart = 16.toPx,
                paddingEnd = 16.toPx,
                rule = Rules.MIDDLE
        ))

        val bindableOffset = BindableController.BindableOffset()

        val decorator2 = Builder()
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