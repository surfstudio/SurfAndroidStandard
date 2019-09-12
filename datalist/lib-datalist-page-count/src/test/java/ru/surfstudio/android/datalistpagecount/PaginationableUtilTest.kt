package ru.surfstudio.android.datalistpagecount

import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.datalistpagecount.util.PaginationableUtil


class PaginationableUtilTest {
    @Test
    fun paginationRequest() {
        val response = (1..10).toList()

        val resultList: DataList<Int> = DataList.emptyUnspecifiedTotal()

        PaginationableUtil.getPaginationRequestPortions<Int>({ page ->
            Observable.just(DataList(arrayListOf(response[page]), page, 1))
        }, 10)
                .subscribe { list -> resultList.merge(list) }

        val expected = DataList(response.subList(0, 10), 0, 10, 1)
        Assert.assertEquals(expected, resultList)
    }

    @Test
    fun paginationSingleRequest() {
        val response = (1..10).toList()

        val resultList: DataList<Int> = DataList.emptyUnspecifiedTotal()

        PaginationableUtil.getPaginationSingleRequestPortion<Int>({ page ->
            Single.just(DataList(arrayListOf(response[page]), page, 1))
        }, 10)
                .subscribe { list -> resultList.merge(list) }

        val expected = DataList(response.subList(0, 10), 0, 10, 1)
        Assert.assertEquals(expected, resultList)
    }
}