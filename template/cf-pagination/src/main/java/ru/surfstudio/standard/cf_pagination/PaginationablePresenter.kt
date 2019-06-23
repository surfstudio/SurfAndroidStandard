package ru.surfstudio.standard.cf_pagination

import androidx.annotation.CallSuper
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * Презентер, поддерживающий пагинацию элементов
 */
abstract class PaginationablePresenter<T>(
        basePresenterDependency: BasePresenterDependency,
        private val bm: PaginationableBindModel<T>
) : BaseRxPresenter(basePresenterDependency) {

    protected var loadDataDisposable = Disposables.disposed()

    @CallSuper
    override fun onFirstLoad() {
        super.onFirstLoad()
        bm.loadNextPage bindTo ::loadNextPage
    }

    abstract fun loadData(offset: Int = 0)

    private fun loadNextPage() {
        with(bm.data) {
            if (hasValue) {
                loadData(value.nextOffset)
            }
        }
    }

    protected fun setNormalPaginationState(elements: DataList<T>) {
        bm.paginationState.accept(
                if (elements.canGetMore())
                    PaginationState.READY
                else
                    PaginationState.COMPLETE
        )
    }

    protected fun setErrorPaginationState(elements: DataList<T>) {
        bm.paginationState.accept(
                if (elements.isEmpty())
                    PaginationState.COMPLETE
                else
                    PaginationState.ERROR
        )
    }
}