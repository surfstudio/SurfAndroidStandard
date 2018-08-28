package ru.surfstudio.android.recycler.extension.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sample.*
import kotlinx.android.synthetic.main.list_item_sample.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.extension.sample.controller.SampleItemController
import ru.surfstudio.android.recycler.extension.sample.controller.SampleStickyHeaderItemController
import ru.surfstudio.android.recycler.extension.sticky.StickyEasyAdapter
import ru.surfstudio.android.recycler.extension.sticky.addStickyHeader

val EXTRA = "EXTRA"

class SampleFragment : Fragment() {

//    private lateinit var stickyEasyAdapter: StickyEasyAdapter
    private var adapter: StickyEasyAdapter? = null
    private val sampleItemController = SampleItemController()
    private val stickyHeaderItemController = SampleStickyHeaderItemController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_sample, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val data = arguments?.get(EXTRA) as? String ?: EXTRA

        fragment_sample_rv.isSaveEnabled = false
        adapter = StickyEasyAdapter(fragment_sample_rv)
        adapter?.setItems(ItemList.create()
                .add(data, sampleItemController)
                .addStickyHeader("Бубульки", stickyHeaderItemController)
        )
    }

    override fun onDestroyView() {
        fragment_sample_rv.adapter = null
        fragment_sample_rv.layoutManager = null
        adapter = null
        super.onDestroyView()
    }
}