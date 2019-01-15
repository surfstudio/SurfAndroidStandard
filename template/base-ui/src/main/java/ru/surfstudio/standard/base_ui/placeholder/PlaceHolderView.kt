package ru.surfstudio.standard.base_ui.placeholder

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView
import ru.surfstudio.android.sample.common.ui.base.loadstate.LoadState

/**
 * Реализация [StandardPlaceHolderView] для приложения.
 *
 * Все состояния обрабатываются стандартно.
 */
@Deprecated(
        message = "Следует использовать PlaceHolderViewContainer",
        replaceWith = ReplaceWith(
                expression = "PlaceHolderViewContainer(context, attrs)",
                imports = ["ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer"]
        )
)
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