package ru.surfstudio.android.datalistlimitoffset

import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
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
        val list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), 10, 7, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
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
        val intList = DataList<Int>(arrayListOf(1,2,3,4,5),5,0,5)
        val stringList = intList.transform { it.toString() }

        val expectedList = DataList<String>(arrayListOf("1","2","3","4","5"),5,0,5)
        Assert.assertEquals(expectedList, stringList)
    }
}


