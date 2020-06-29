package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.ui.view.keyboard.KeyBoardConsts.createKeyBoarWithRightBtn
import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey
import ru.surfstudio.standard.ui.view.keyboard.keys.IconKey
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey
import ru.unicredit.android.ui.view.keyboard.controller.EmptyKeyController
import ru.unicredit.android.ui.view.keyboard.controller.IconController
import ru.unicredit.android.ui.view.keyboard.controller.KeyController

class KeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {

    private val easyAdapter = EasyAdapter()

    private val keyController = KeyController { /*todo */ }
    private val emptyController = EmptyKeyController()
    private val deleteController = IconController { /*todo*/ }

    init {
        adapter = easyAdapter

        with(ItemList.create()) {
            createKeyBoarWithRightBtn(IconKey(R.drawable.ic_android/*todo*/)).forEach {
                when (it) {
                    is TextKey -> add(it, keyController)
                    is EmptyKey -> add(it, emptyController)
                    is IconKey -> add(it, deleteController)
                }
            }
            easyAdapter.setItems(this)
        }
    }
}
