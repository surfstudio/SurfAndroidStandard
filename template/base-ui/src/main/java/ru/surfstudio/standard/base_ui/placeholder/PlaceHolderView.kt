package ru.surfstudio.standard.base_ui.placeholder

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView
import ru.surfstudio.android.sample.common.ui.base.placeholder.LoadState

/**
 * Реализация [StandardPlaceHolderView] для приложения.
 *
 * Все состояния обрабатываются стандартно.
 */
class PlaceHolderView(
        context: Context,
        attributeSet: AttributeSet
) : StandardPlaceHolderView(context, attributeSet), PlaceHolderViewInterface {

    override fun render(loadState: LoadStateInterface) {
        when (loadState) {
            LoadState.NONE -> setNoneState()
            LoadState.MAIN_LOADING -> setMainLoadingState()
            LoadState.TRANSPARENT_LOADING -> setTransparentLoadingState()
            LoadState.EMPTY -> setEmptyState()
            LoadState.ERROR -> setErrorState()
            LoadState.NOT_FOUND -> setNotFoundState()
            LoadState.NO_INTERNET -> setNoInternetState()
        }
    }
}