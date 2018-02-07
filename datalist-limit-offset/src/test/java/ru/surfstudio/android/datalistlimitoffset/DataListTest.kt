package ru.surfstudio.android.datalistlimitoffset

import org.junit.Assert
import org.junit.Test
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import java.util.*

class DataListTest {

    @Test
    fun checkNormalMerge() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 0, 5,5)
        var list2: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 5, 5,5)
        var list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), 0, 10, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkNormalMergeWIthOffset() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 7, 5, 10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(6, 7, 8, 9, 10)), 12, 5, 10)
        var list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), 7, 10, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 0, 3, 10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 0, 3, 10)
        var list3: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 0, 3, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision2() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 2, 3,10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(4, 5)), 4, 3,10)
        var list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 4, 5)), 2, 5,10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision3() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 0, 5, 10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 0, 3, 10)
        var list3: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 0, 3, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test
    fun checkMergeWithCollision4() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(6, 7)), 0, 3, 10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 0, 5, 10)
        var list3: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3, 4, 5)), 0, 5, 10)
        list1.merge(list2)
        Assert.assertEquals(list1, list3)
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkInvalidData() {
        var list1: DataList<Int> = DataList(ArrayList(listOf(1, 2, 3)), 0, 3, 10)
        var list2: DataList<Int> = DataList(ArrayList(listOf(4, 5)), 4, 3, 10)
        list1.merge(list2)
    }


}


