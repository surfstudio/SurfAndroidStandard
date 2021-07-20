package ru.surfstudio.standard.i_auth

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

internal class SampleUnitTest : AnnotationSpec() {

    @Test
    fun `sample - when a and b equals 2, sum should be 4`() {
        (2 + 2) shouldBe 4
    }
}