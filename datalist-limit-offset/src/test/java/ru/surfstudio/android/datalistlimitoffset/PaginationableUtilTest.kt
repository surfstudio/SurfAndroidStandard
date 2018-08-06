package ru.surfstudio.android.datalistlimitoffset

import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.datalistlimitoffset.util.PaginationableUtil

class PaginationableUtilTest {

    @Test
    fun paginationableRequest() {
        val response = (1..100).toList()

        val resultList: DataList<Int> = DataList.empty()

        PaginationableUtil.getPaginationRequestPortions<Int>({ blockSize: Int, offset: Int ->
            Observable.just(DataList(arrayListOf(response[offset]), blockSize, offset))
        }, 0, 10, 1)
                .subscribe {
                    resultList.merge(it)
                }

        val expected = DataList(response.subList(0, 10), 10, 0)
        Assert.assertEquals(expected, resultList)
    }

    @Test
    fun paginationableRequestWithTotalCount() {
        val response = (1..100).toList()

        val resultList: DataList<Int> = DataList.emptyWithTotal(10)

        PaginationableUtil.getPaginationRequestPortionsWithTotal<Int>({ blockSize: Int, offset: Int ->
            Observable.just(DataList(arrayListOf(response[offset]), blockSize, offset))
        }, 0, 10, 1, 10)
                .subscribe {
                    if (resultList.canGetMore()) resultList.merge(it)
                }

        val expected = DataList(response.subList(0, 10), 10, 0)
        Assert.assertEquals(expected, resultList)
    }

    @Test
    fun getPaginationRequestSinglePortionWithTotal() {
        val response = (1..100).toList()

        val resultList: DataList<Int> = DataList.emptyWithTotal(10)

        PaginationableUtil.getPaginationRequestSinglePortionWithTotal<Int>({ blockSize: Int, offset: Int ->
            Single.just(DataList(arrayListOf(response[offset]), blockSize, offset))
        }, 0, 10, 1, 10)
                .subscribe { it ->
                    if (resultList.canGetMore()) resultList.merge(it)
                }

        val expected = DataList(response.subList(0, 10), 10, 0)
        Assert.assertEquals(expected, resultList)
    }

    @Test
    fun getPaginationSingleRequestPortion() {
        val response = (1..100).toList()

        val resultList: DataList<Int> = DataList.empty()

        PaginationableUtil.getPaginationSingleRequestPortion<Int>({ blockSize: Int, offset: Int ->
            Single.just(DataList(arrayListOf(response[offset]), blockSize, offset))
        }, 0, 10, 1)
                .subscribe { it ->
                    resultList.merge(it)
                }

        val expected = DataList(response.subList(0, 10), 10, 0)
        Assert.assertEquals(expected, resultList)
    }
}