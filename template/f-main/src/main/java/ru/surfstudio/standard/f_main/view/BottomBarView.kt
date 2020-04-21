package ru.surfstudio.standard.f_main.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.layout_bottom_bar.view.*
import ru.surfstudio.android.template.f_main.R
import ru.surfstudio.standard.ui.navigation.MainTabType

/**
 * Кастомный BottomBar главного экрана.
 *
 * Виджет предоставляет альтернативную реализацию BottomNavigationView из SDK
 * с возможностью использовать анимированные изображения и установку каунтеров.
 */
class BottomBarView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val tabButtonsMap by lazy {
        hashMapOf<MainTabType, BottomBarItemView>(
                MainTabType.FEED to bottom_bar_feed_btn,
                MainTabType.SEARCH to bottom_bar_search_btn,
                MainTabType.PROFILE to bottom_bar_profile_btn
        )
    }

    /**
     * Экшн, вызываемый при выборе таба
     */
    var tabSelectedAction: (MainTabType) -> Unit = {  }

    init {
        View.inflate(context, R.layout.layout_bottom_bar, this)

        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        initListeners()
        updateSelection()
    }

    /**
     * Установка нажатого состояния выбранного таба.
     *
     * @param selectedTabType тип выбранного таба
     */
    fun updateSelection(selectedTabType: MainTabType = MainTabType.FEED) {
        for ((tabType, button) in tabButtonsMap) {
            button.setChecked(tabType == selectedTabType)
        }
    }

    /**
     * Установка счетчика на таб
     *
     * @param value значение счетчика
     * @param tabType тип таба, на который устанавливать
     */
    fun setCounterOnTab(value: Int, tabType: MainTabType) {
        tabButtonsMap[tabType]?.setCounter(value)
    }

    /**
     * @param isVisible true, если бейдж нужно показать, false если скрыть
     */
    fun setBadgeVisibilityOnTab(isVisible: Boolean, tabType: MainTabType){
        tabButtonsMap[tabType]?.setBadgeVisibility(isVisible)
    }

    private fun initListeners() {
        for ((tabType, button) in tabButtonsMap) {
            button.setOnClickListener { tabSelectedAction(tabType) }
        }
    }
}