package ru.surfstudio.android.recycler.decorator.sample.list

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.surfstudio.android.recycler.decorator.sample.pager.controllers.Controller
import kotlinx.android.synthetic.main.activity_recycler.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.list.decor.*
import ru.surfstudio.android.recycler.decorator.sample.toPx

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LinearDecoratorActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()

    private val shortCardController = Controller(R.layout.item_controller_short_card)
    private val longCardController = Controller(R.layout.item_controller_long_card)
    private val spaceController = Controller(R.layout.item_controller_space)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        init()
    }

    private fun init() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@LinearDecoratorActivity)
            adapter = easyAdapter
        }

        val simpleOffsetDrawer1 = SimpleOffsetDrawer(
            left = 16.toPx,
            top = 8.toPx,
            right = 16.toPx,
            bottom = 8.toPx
        )

        val simpleOffsetDrawer2 = SimpleOffsetDrawer(
            left = 16.toPx,
            right = 16.toPx
        )

        val dividerDrawer2Px = LinearOverDividerDrawer(
            Gap(
                resources.getColor(R.color.gray_A150),
                2.toPx,
                paddingStart = 16.toPx,
                paddingEnd = 16.toPx,
                rule = Rules.MIDDLE
            )
        )

        val roundViewHoldersGroupDrawer = RoundViewHoldersGroupDrawer(12.toPx.toFloat())
        val paralaxdecorator = ParallaxHeaderDecoration(this, R.drawable.night_png)

        val decorator2 = Decorator.Builder()
            .underlay(longCardController.viewType() to roundViewHoldersGroupDrawer)
            .underlay(paralaxdecorator)
            .overlay(shortCardController.viewType() to dividerDrawer2Px)
            .offset(longCardController.viewType() to simpleOffsetDrawer1)
            .offset(shortCardController.viewType() to simpleOffsetDrawer2)
            .offset(spaceController.viewType() to simpleOffsetDrawer1)
            .build()

        recycler_view.addItemDecoration(decorator2)

        val items = ItemList.create()
        (0..1).forEach { _ ->
            items.add(longCardController)
        }
        items.add(spaceController)

        (0..3).forEach { _ ->
            items.add(longCardController)
        }

        (0..10).forEach { _ ->
            items.add(shortCardController)
        }
        easyAdapter.setItems(items)
    }
}
