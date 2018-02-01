package ru.surfstudio.standard.ui.screen.main


import ru.surfstudio.android.core.domain.Unit
import ru.surfstudio.android.core.domain.datalist.DataList
import ru.surfstudio.android.core.ui.base.screen.model.LdsPgnScreenModel
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState

data class MainScreenModel(
        var list: DataList<Unit> = DataList.empty()
) : LdsPgnScreenModel() {


    fun replaceData(newList: DataList<Unit>) {
        list = newList
    }

    fun hideLoadingNormal() {
        hideLoading(LoadState.EMPTY)
    }

    fun hideLoading(noDataState: LoadState) {
        loadState = if (list.isEmpty()) noDataState else LoadState.NONE
    }

    fun hideLoadingError() {
        hideLoading(LoadState.ERROR)
    }

    fun showLoading(minimalLoadingState: LoadState) {
        if (list.isEmpty()) LoadState.MAIN_LOADING else minimalLoadingState
    }

    fun showLoading() {
        showLoading(LoadState.SMALL_LOADING)
    }
}