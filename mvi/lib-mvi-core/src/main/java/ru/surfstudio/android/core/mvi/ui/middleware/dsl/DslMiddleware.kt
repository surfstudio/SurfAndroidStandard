package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.Middleware

interface DslMiddleware<T : Event, InputStream, OutputStream, TransformList : List<OutputStream>> : Middleware<T, InputStream, OutputStream> {

    fun provideTransformationList(eventStream: InputStream): TransformList


    /**
     * Трансформация потока событий с помощью DSL
     */
    fun transformations(
            eventStream: InputStream,
            eventStreamBuilder: TransformList.() -> Unit
    ): OutputStream {
        val streamTransformers = provideTransformationList(eventStream)
        eventStreamBuilder.invoke(streamTransformers)
        return combineTransformations(streamTransformers)
    }

    /**
     * Комбинация списка трансформаций в один поток,
     * для дальнейшего прикрепления к главному потоку событий.
     */
    fun combineTransformations(transformations: List<OutputStream>): OutputStream

}