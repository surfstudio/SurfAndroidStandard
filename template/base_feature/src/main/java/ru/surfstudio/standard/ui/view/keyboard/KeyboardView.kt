package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.ui.view.keyboard.KeyBoardConsts.createKeyBoarWithRightBtn
import ru.surfstudio.standard.ui.view.keyboard.controller.EmptyKeyController
import ru.surfstudio.standard.ui.view.keyboard.controller.IconController
import ru.surfstudio.standard.ui.view.keyboard.controller.KeyController
import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey
import ru.surfstudio.standard.ui.view.keyboard.keys.IconKey
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

class KeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {

    private val easyAdapter = EasyAdapter().apply { setFirstInvisibleItemEnabled(true) }


    private val keyController = KeyController { /*todo */ }
    private val emptyController = EmptyKeyController()
    private val deleteController = IconController { /*todo*/ }

    init {
        layoutManager = GridLayoutManager(context, KEYBOARD_COLUMNS_COUNT)
        overScrollMode = View.OVER_SCROLL_NEVER
        adapter = easyAdapter

        setBackgroundColor(Color.RED)

        create()
    }

    private fun create() {
        val keys = createKeyBoarWithRightBtn(IconKey(R.drawable.ic_android/*todo*/))
        easyAdapter.setItems(
                ItemList.create().apply {
                    keys.forEach {
                        when (it) {
                            is TextKey -> add(it, keyController)
                            is IconKey -> add(it, deleteController)
                            is EmptyKey -> add(it, emptyController)
                        }
                    }
                }
        )
    }

    companion object {

        const val KEYBOARD_COLUMNS_COUNT = 3
    }
}
