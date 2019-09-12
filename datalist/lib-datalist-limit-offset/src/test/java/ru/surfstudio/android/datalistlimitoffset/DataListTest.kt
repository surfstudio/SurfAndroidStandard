package ru.surfstudio.android.datalistlimitoffset

import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.datalistlimitoffset.util.emptyDataListOf
import ru.surfstudio.android.datalistlimitoffset.util.filter
import ru.surfstudio.android.datalistlimitoffset.util.map
import java.util.*

class DataListTest {

    @Test
    fun checkNormalMerge() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 5, 5)
        val list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), 10, 0, 10)
        list1.merge(list2)
        Assert.assertEquals(list3, list1)

    }

    @Test
    fun checkNormalMergeWIthOffset() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 7, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 12, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 17, 10)
        val list4: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 6, 7, 8, 9, 10)), 15, 7, 10)
        list3.merge(list2)
        list3.merge(list1)
        Assert.assertEquals(list3, list4)
    }

    @Test
    fun checkNormalMergeWithOffsetAndCollision() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 7, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 12, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 14, 10)
        val list4: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 6, 7, 8, 9, 10)), 12, 7, 10)
        list3.merge(list2)
        list3.merge(list1)
        Assert.assertEquals(list3, list4)
    }

    @Test
    fun checkMergeWithCollision() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 3, 0, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 3, 0, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 3, 0, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision2() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 3, 2, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(4, 5)), 3, 4, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 4, 5)), 5, 2, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision3() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 3, 0, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 3, 0, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision4() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 3, 0, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeEmptyWithNormal() {
        val list1: DataList<Int> = DataList.empty()
        val list2: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)
        val list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 5, 0, 10)

        list1.merge(list2)
        Assert.assertEquals(list3, list1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkInvalidData() {
        val list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 3, 0, 10)
        val list2: DataList<Int> = DataList(ArrayList(listOf(4, 5)), 3, 4, 10)
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
    fun checkDynamicDataInsertion() {
        val element1 = Element(1)
        val element2 = Element(2)
        val element3 = Element(3)
        val element4 = Element(4)
        val element5 = Element(5)
        val element6 = Element(6)
        val element7 = Element(7)
        val element8 = Element(8)

        val list1 = DataList(arrayListOf(
                element1, element2, element3, element4, element5
        ), 5, 4, 20)

        val list2 = DataList(arrayListOf(
                element4, element5, element6, element7, element8
        ), 5, 9, 10)

        list1.merge(list2) { it.id }

        val list3 = DataList(arrayListOf(
                element1,
                element2,
                element3,
                element4,
                element5,
                element6,
                element7,
                element8
        ), 10, 4, 20)
        Assert.assertEquals(list3, list1)
    }

    @Test
    fun checkDynamicDataInsertion2() {
        val element1 = Element(1)
        val element2 = Element(2)
        val element3 = Element(3)
        val element4 = Element(4)
        val element5 = Element(5)
        val element6 = Element(6)
        val element7 = Element(7)

        val list1 = DataList(arrayListOf(
                element1, element2, element3, element4, element5, element6, element7
        ), 7, 6, 30)

        val list2 = DataList(arrayListOf(
                element1, element2, element3, element4, element5, element6, element7
        ), 7, 13, 30)

        val list3 = DataList(arrayListOf(
                element1, element2, element3, element4, element5, element6, element7
        ), 7, 20, 30)

        list1.merge(list2) { it.id }
        list1.merge(list3) { it.id }

        val list4 = DataList(arrayListOf(
                element1,
                element2,
                element3,
                element4,
                element5,
                element6,
                element7
        ), 21, 6, 30)

        Assert.assertEquals(list1, list4)
    }

    @Test
    fun checkDynamicDataInsertion3() {
        val element0 = Element(0)
        val element1 = Element(1)
        val element2 = Element(2)
        val element3 = Element(3)
        val element4 = Element(4)
        val element5 = Element(5)
        val element6 = Element(6)
        val element7 = Element(7)
        val element8 = Element(8)
        val element9 = Element(9)

        val list1 = DataList(arrayListOf(
                element1, element2, element3, element4, element5
        ), 5, 4, 30)

        val list2 = DataList(arrayListOf(
                element0, element1, element2, element3, element4
        ), 5, 9, 30)

        val list3 = DataList(arrayListOf(
                element5, element6, element7, element8, element9
        ), 5, 14, 30)

        list1.merge(list2) { it.id }
        list1.merge(list3) { it.id }

        val list4 = DataList(arrayListOf(
                element1,
                element2,
                element3,
                element4,
                element5,
                element0,
                element6,
                element7,
                element8,
                element9
        ), 15, 4, 30)

        Assert.assertEquals(list1, list4)
    }

    private class Element(val id: Int) {
        override fun toString(): String {
            return "Element: $id"
        }
    }
}


