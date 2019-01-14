package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface Relation<V, in S : RelationEntity, in T : RelationEntity> {

    val value: V

    val hasValue: Boolean

    fun getConsumer(source: S): Consumer<V>

    fun getObservable(target: T): Observable<V>
}

interface Related<S : RelationEntity> {

    fun relationEntity(): S

    fun <V> Relation<V, *, S>.getObservable() =
        this.getObservable(relationEntity())

    fun <V> Relation<V, S, *>.getConsumer() =
            this.getConsumer(relationEntity())
}

interface RelationEntity

interface ActionSource : RelationEntity
interface ActionTarget : RelationEntity

interface CommandSource : RelationEntity
interface CommandTarget : RelationEntity

interface StateSource : RelationEntity
interface StateTarget : RelationEntity

object VIEW : ActionSource, CommandTarget, StateSource, StateTarget
object PRESENTER : ActionTarget, CommandSource, StateSource, StateTarget
