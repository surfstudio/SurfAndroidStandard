package ru.surfstudio.android.datalistlimitoffset

import io.reactivex.Observable
import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.datalistlimitoffset.util.PaginationableUtil


class PaginationableUtilTest {
    @Test
    fun paginationableRequest() {
        val response = (1..100).toList()

        var resultList: DataList<Int> = DataList.empty()

        PaginationableUtil.getPaginationRequestPortions<Int>({ blockSize: Int, offset: Int ->
            Observable.just(DataList(arrayListOf(response[offset]), blockSize, 1))
        }, 0, 1,1)
                .subscribe {
                    resultList.merge(it)
                }

        val expected = DataList(response.subList(0,10), 10,0)
        Assert.assertEquals(expected, resultList)
    }
}