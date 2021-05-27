package ru.surfstudio.android.recycler.extension.sample.screen

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_snap_helpers.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.controller.SampleSnapHelperItemController
import ru.surfstudio.android.recycler.extension.snaphelper.GravityPagerSnapHelper
import ru.surfstudio.android.recycler.extension.snaphelper.GravitySnapHelper

class SnapHelpersActivity : AppCompatActivity() {

    private val easyAdapter = EasyAdapter()
    private val controller = SampleSnapHelperItemController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_helpers)

        gravity_snap_helper_rv.apply {
            adapter = easyAdapter
            easyAdapter.setData(createItemList(), controller)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            GravitySnapHelper(Gravity.END).attachToRecyclerView(this)
        }
        gravity_pager_snap_helper_rv.apply {
            adapter = easyAdapter
            easyAdapter.setData(createItemList(), controller)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            GravityPagerSnapHelper(Gravity.END, context).attachToRecyclerView(this)
        }
    }

    private fun createItemList(): List<String> = (0..20).map { index -> "Item #$index" }
}