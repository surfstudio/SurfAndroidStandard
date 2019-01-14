package ru.surfstudio.android.core.mvp.rx.domain

import androidx.annotation.CallSuper
import org.junit.Before

abstract class BaseRelationTest {

    lateinit var testView: Related<VIEW>
    lateinit var testPresenter: Related<PRESENTER>

    @Before
    @Throws(Exception::class)
    @CallSuper
    open fun setUp() {
        testView = object : Related<VIEW> {
            override fun relationEntity(): VIEW = VIEW
        }

        testPresenter = object : Related<PRESENTER> {
            override fun relationEntity(): PRESENTER = PRESENTER
        }
    }
}