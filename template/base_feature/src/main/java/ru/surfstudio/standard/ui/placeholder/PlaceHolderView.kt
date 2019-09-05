package ru.surfstudio.standard.ui.placeholder

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView

/**
 * Реализация [StandardPlaceHolderView] для приложения.
 *
 * Все состояния обрабатываются стандартно.
 */
class PlaceHolderView(
        context: Context,
        attributeSet: AttributeSet
) : StandardPlaceHolderView(context, attributeSet), LoadStateRendererInterface {

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