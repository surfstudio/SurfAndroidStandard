package ru.surfstudio.android.datalistlimitoffset

import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.datalistlimitoffset.util.emptyDataListOf
import ru.surfstudio.android.datalistlimitoffset.util.filter
import ru.surfstudio.android.datalistlimitoffset.util.map

class DataListTest {

    @Test
    fun checkNormalMerge() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)
        val list2: DataList<Int> = DataList(arrayListOf(6, 7, 8, 9, 10), 5, 5, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 10, 0, 10)
        Assert.assertEquals(expected, list1)

    }

    @Test
    fun checkNormalMergeWithOffset() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 7, 10)
        val list2: DataList<Int> = DataList(arrayListOf(6, 7, 8, 9, 10), 5, 12, 10)
        val list3: DataList<Int> = DataList(arrayListOf(11, 12, 13, 14, 15), 5, 17, 10)

        list1.merge(list2)
        list1.merge(list3)

        val expected: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15), 15, 7, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkNormalMergeWithOffsetAndCollision() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 7, 10)
        val list2: DataList<Int> = DataList(arrayListOf(6, 7, 8, 9, 10), 5, 12, 10)
        val list3: DataList<Int> = DataList(arrayListOf(11, 12, 13, 14, 15), 5, 14, 10)

        list1.merge(list2)
        list1.merge(list3)

        val expected: DataList<Int> = DataList(
                arrayListOf(1, 2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 15),
                12,
                7,
                10
        )
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkMergeWithCollisionOnStart() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3), 3, 0, 10)
        val list2: DataList<Int> = DataList(arrayListOf(4, 5), 3, 0, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(4, 5), 3, 0, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkMergeWithCollisionInMiddle() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3), 3, 2, 10)
        val list2: DataList<Int> = DataList(arrayListOf(4, 5), 3, 4, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(1, 2, 4, 5), 5, 2, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkMergeWithCollisionInStartLimitSmallerThanSourceList() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)
        val list2: DataList<Int> = DataList(arrayListOf(6, 7), 2, 0, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(6, 7), 2, 0, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkMergeWithCollisionInStartLimitBiggerThanSourceList() {
        val list1: DataList<Int> = DataList(arrayListOf(6, 7), 2, 0, 10)
        val list2: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkMergeEmptyWithNormal() {
        val list1: DataList<Int> = DataList.empty()
        val list2: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)

        list1.merge(list2)

        val expected: DataList<Int> = DataList(arrayListOf(1, 2, 3, 4, 5), 5, 0, 10)
        Assert.assertEquals(expected, list1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkInvalidRange() {
        val list1: DataList<Int> = DataList(arrayListOf(1, 2, 3), 3, 0, 10)
        val list2: DataList<Int> = DataList(arrayListOf(4, 5), 3, 4, 10)
        assert(list1.limit + list1.offset < list2.offset) //there's a gap between ranges
        list1.merge(list2)
    }

    @Test
    fun transformToStringDataList() {
        val intList = DataList<Int>(arrayListOf(1, 2, 3, 4, 5), 5, 0, 5)
        val stringList = intList.transform { it.toString() }

        val expectedList = DataList<String>(arrayListOf("1", "2", "3", "4", "5"), 5, 0, 5)
        Assert.assertEquals(expectedList, stringList)
    }

    @Test
    fun checkMapExtensionDataList() {
        val intList = DataList<Int>(arrayListOf(1, 2, 3, 4, 5), 5, 0, 5)
        val stringList = intList.map(Int::toString)

        val expectedList = DataList<String>(arrayListOf("1", "2", "3", "4", "5"), 5, 0, 5)
        Assert.assertEquals(expectedList, stringList)
    }

    @Test
    fun checkFilterExtensionDataList() {
        val negativeList = DataList<Int>(arrayListOf(-1, -2, -3, -4, -5), 5, 0, 5)
        val filteredList = negativeList.filter { it > 0 }

        val emptyList = emptyDataListOf<Int>()
        Assert.assertEquals(emptyList.size, filteredList.size)
    }

    @Test
    fun checkFilteringDuplicatesSimple() {
        val e1 = Element(1)
        val e2 = Element(2)
        val e3 = Element(3)
        val e4 = Element(4)
        val e5 = Element(5)
        val e6 = Element(6)
        val e7 = Element(7)
        val e8 = Element(8)

        val list1 = DataList(arrayListOf(
                e1, e2, e3, e4, e5
        ), 5, 4, 20)

        val list2 = DataList(arrayListOf(
                e4, e5, e6, e7, e8
        ), 5, 9, 10)

        list1.merge(list2) { it.id }

        val expected = DataList(arrayListOf(
                e1,
                e2,
                e3,
                e4,
                e5,
                e6,
                e7,
                e8
        ), 10, 4, 20)
        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkFilteringDuplicates() {
        val e1 = Element(1)
        val e2 = Element(2)
        val e3 = Element(3)
        val e4 = Element(4)
        val e5 = Element(5)
        val e6 = Element(6)
        val e7 = Element(7)

        val list1 = DataList(arrayListOf(
                e1, e2, e3, e4, e5, e6, e7
        ), 7, 6, 30)

        val list2 = DataList(arrayListOf(
                e1, e2, e3, e4, e5, e6, e7
        ), 7, 13, 30)

        val list3 = DataList(arrayListOf(
                e1, e2, e3, e4, e5, e6, e7
        ), 7, 20, 30)

        list1.merge(list2) { it.id }
        list1.merge(list3) { it.id }

        val expected = DataList(arrayListOf(
                e1,
                e2,
                e3,
                e4,
                e5,
                e6,
                e7
        ), 21, 6, 30)

        Assert.assertEquals(expected, list1)
    }

    @Test
    fun checkFilteringDuplicatesMultiple() {
        val e0 = Element(0)
        val e1 = Element(1)
        val e2 = Element(2)
        val e3 = Element(3)
        val e4 = Element(4)
        val e5 = Element(5)
        val e6 = Element(6)
        val e7 = Element(7)
        val e8 = Element(8)
        val e9 = Element(9)

        val list1 = DataList(arrayListOf(
                e1, e2, e3, e4, e5
        ), 5, 4, 30)

        val list2 = DataList(arrayListOf(
                e0, e1, e2, e3, e4
        ), 5, 9, 30)

        val list3 = DataList(arrayListOf(
                e5, e6, e7, e8, e9
        ), 5, 14, 30)

        list1.merge(list2) { it.id }
        list1.merge(list3) { it.id }

        val list4 = DataList(arrayListOf(
                e1,
                e2,
                e3,
                e4,
                e5,
                e0,
                e6,
                e7,
                e8,
                e9
        ), 15, 4, 30)

        Assert.assertEquals(list1, list4)
    }

    private class Element(val id: Int) {
        override fun toString(): String {
            return "Element: $id"
        }
    }
}


