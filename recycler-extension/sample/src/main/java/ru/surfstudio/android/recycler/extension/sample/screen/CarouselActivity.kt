package ru.surfstudio.android.recycler.extension.sample.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_carousel.*
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.extension.CarouselView
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.controller.SampleItemController
import ru.surfstudio.android.recycler.extension.sample.domain.Data
import ru.surfstudio.android.recycler.extension.snaphelper.PagerSnapHelper

/**
 * Sample using of [CarouselView]
 */
class CarouselActivity : AppCompatActivity() {

    private val itemController = SampleItemController(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel)
        with(sample_carousel_view) {
            setSnapHelper(PagerSnapHelper(context))
            render(generateData())
        }
    }

    private fun generateData() = ItemList().apply {
        add(Data("Scooby-Doo", "Main characters"), itemController)
        add(Data("Shaggy Rogers", "Main characters"), itemController)
        add(Data("Fred Jones", "Main characters"), itemController)
        add(Data("Daphne Blake", "Main characters"), itemController)
        add(Data("Velma Dinkley", "Main characters"), itemController)
        add(Data("Scrappy-Doo", "Secondary characters"), itemController)
        add(Data(LAST_ITEM_TITLE, ""), itemController)
    }
}