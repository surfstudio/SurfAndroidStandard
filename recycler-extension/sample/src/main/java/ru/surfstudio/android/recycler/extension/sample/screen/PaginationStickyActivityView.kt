package ru.surfstudio.android.recycler.extension.sample.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pagination_sticky_view.*
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.adapter.PaginationStickyEasyAdapter
import ru.surfstudio.android.recycler.extension.sample.controller.SampleItemController
import ru.surfstudio.android.recycler.extension.sample.controller.SampleStickyItemController
import ru.surfstudio.android.recycler.extension.sample.domain.Data
import ru.surfstudio.android.recycler.extension.sample.interactor.DataRepository
import ru.surfstudio.android.recycler.extension.sticky.addStickyHeader

class PaginationStickyActivityView : AppCompatActivity() {

    private val repository = DataRepository()

    private lateinit var adapter: PaginationStickyEasyAdapter
    private val dataItemController = SampleItemController()
    private val stickyDataItemController = SampleStickyItemController()

    private val list: DataList<Data> = DataList.empty()
    private var page = 1
    private var paginationState = PaginationState.READY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination_sticky_view)

        initViews()
        loadMore()
    }

    private fun initViews() {
        adapter = PaginationStickyEasyAdapter(pagination_sticky_rv) {
            loadMore()
        }
    }

    private fun loadMore() {
        list.merge(repository.getDataByPage(page++))

        if (page == DataRepository.PAGES_COUNT + 1) {
            paginationState = PaginationState.COMPLETE
        }

        adapter.setItems(
                ItemList.create().apply {
                    list.forEachIndexed { index, data ->
                        if ((index + 1) % 25 == 0) {
                            addStickyHeader(data, stickyDataItemController)
                        } else {
                            add(data, dataItemController)
                        }
                    }
                },
                paginationState
        )
    }
}