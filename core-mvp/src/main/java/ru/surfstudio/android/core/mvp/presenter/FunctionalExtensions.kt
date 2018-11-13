package ru.surfstudio.android.core.mvp.presenter

import ru.surfstudio.android.rx.extension.ActionSafe
import ru.surfstudio.android.rx.extension.ConsumerSafe


fun <T> ((T) -> Unit).asConsumerSafe(): ConsumerSafe<T> {
    return ConsumerSafe { this.invoke(it) }
}

fun ((Throwable) -> Unit).asErrorConsumerSafe(): ConsumerSafe<Throwable> {
    return ConsumerSafe { this.invoke(it) }
}

fun (() -> Unit).asActionSafe(): ActionSafe {
    return ActionSafe { this.invoke() }
}

fun <T> ConsumerSafe<T>.fromConsumer(): (T) -> Unit {
    return object : (T) -> Unit {

        override fun invoke(p1: T) {
            this@fromConsumer.accept(p1)
        }
    }
}

fun ConsumerSafe<Throwable>.fromErrorConsumer(): (Throwable) -> Unit {
    return object : (Throwable) -> Unit {

        override fun invoke(p1: Throwable) {
            this@fromErrorConsumer.accept(p1)
        }
    }
}

fun ActionSafe.fromCompleteAction(): () -> Unit {
    return object : () -> Unit {

        override fun invoke() {
            this@fromCompleteAction.run()
        }
    }
}
