package ru.surfstudio.standard.i_auth

import org.junit.Test

class SampleUnitTest {
    @Test
    fun testOne() {
        System.out.println("testOne")

        val i = 0
    }

    @Test
    fun testTwo() {
        System.out.println("testTwo")
    }

    @Test
    fun testThree() {
        System.out.println("testThree")
    }

    @Test
    fun testFour() {
        System.out.println("testFour")
        throw RuntimeException("Checked Exception. Test success")
    }
}