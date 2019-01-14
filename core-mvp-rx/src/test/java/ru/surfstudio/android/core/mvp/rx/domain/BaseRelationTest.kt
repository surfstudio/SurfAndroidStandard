package ru.surfstudio.android.core.mvp.rx.domain

import androidx.annotation.CallSuper
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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