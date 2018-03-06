package ru.surfstudio.standard.ui.screen.main

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView

class PlaceHolderView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet,
                                                defStyleAttr: Int = 0)
    : StandardPlaceHolderView(context, attrs, defStyleAttr), PlaceHolderViewInterface {

    override fun render(loadState: LoadState) {
        when (loadState) {
            LoadState.NONE -> {
                setNoneState()
            }
            LoadState.MAIN_LOADING -> {
                setMainLoadingState()
            }
            LoadState.TRANSPARENT_LOADING -> {
                setTransparentLoadingState()
            }
            LoadState.EMPTY -> {
                setEmptyState()
            }
            LoadState.ERROR -> {
                setErrorState()
            }
            LoadState.NOT_FOUND -> {
                setNotFoundState()
            }
        }
    }
}