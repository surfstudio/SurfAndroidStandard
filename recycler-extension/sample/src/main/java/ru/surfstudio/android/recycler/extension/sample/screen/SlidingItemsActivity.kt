package ru.surfstudio.android.recycler.extension.sample.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sliding_items.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.controller.SlidingItemController
import ru.surfstudio.android.recycler.extension.slide.SlidingHelper

/**
 * Sample of using Sliding ViewHolders
 * */
internal class SlidingItemsActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()
    private val slidingHelper = SlidingHelper()
    private val slidingItemController = SlidingItemController(
            onShareClicked = { /* do stuff */ },
            onFavoritesClicked = { /* do stuff */ },
            onDeleteClicked = { /* do stuff */ }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sliding_items)
        sliding_items_rv.apply {
            slidingHelper.bind(this)
            adapter = easyAdapter
            easyAdapter.setItems(createItemList())
        }
    }

    override fun onDestroy() {
        slidingHelper.unbind()
        super.onDestroy()
    }

    private fun createItemList(): ItemList {
        val items = (0..20).map { index -> "Item #$index" }
        return ItemList.create().addAll(items, slidingItemController)
    }
}