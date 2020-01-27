package ru.surfstudio.android.recycler.decorator.sample

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.surfstudio.android.recycler.decorator.sample.controllers.Controller
import kotlinx.android.synthetic.main.activity_linear_decoration.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Builder
import ru.surfstudio.android.recycler.decorator.sample.sample.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LinearDecoratorActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()

    private val shortCardController = Controller(R.layout.item_controller_short_card)
    private val longCardController = Controller(R.layout.item_controller_long_card)
    private val spaceController = Controller(R.layout.item_controller_space)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_decoration)
        init()
    }

    private fun init() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@LinearDecoratorActivity)
            adapter = easyAdapter
        }

        val simpleOffsetDrawer1 = SimpleOffsetDrawer(
            left = 16.px,
            top = 8.px,
            right = 16.px,
            bottom = 8.px
        )

        val simpleOffsetDrawer2 = SimpleOffsetDrawer(
            left = 16.px,
            right = 16.px
        )

        val dividerDrawer2Px = LinearOverDividerDrawer(
            Gap(
                resources.getColor(R.color.gray_A150),
                2.px,
                paddingStart = 16.px,
                paddingEnd = 16.px,
                rule = Rules.MIDDLE
            )
        )

        val roundViewHoldersGroupDrawer = RoundViewHoldersGroupDrawer(12.px.toFloat())
        val paralaxdecorator = ParallaxHeaderDecoration(this, R.drawable.night_png)

        val decorator2 = Builder()
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
