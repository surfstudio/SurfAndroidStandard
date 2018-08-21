package ru.surfstudio.android.filestorage.sample.ui.base.placeholder

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView

class PlaceHolderView(context: Context, attributeSet: AttributeSet
) : StandardPlaceHolderView(context, attributeSet), PlaceHolderViewInterface {

    override fun render(loadState: LoadState?) {
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